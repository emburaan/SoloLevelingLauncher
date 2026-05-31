package com.sumit.launcher.ui.presentation.searchscreen

import android.content.Context
import androidx.lifecycle.ViewModel
import com.sumit.launcher.data.apps.InstalledAppsRepository
import com.sumit.launcher.data.focus.AppFocusState
import com.sumit.launcher.domain.focus.ObserveAppFocusStateUseCase
import com.sumit.launcher.domain.focus.UpdateAppFocusUseCase
import com.sumit.launcher.domain.launch.DecideAppLaunchUseCase
import com.sumit.launcher.domain.launch.LaunchDecision
import com.sumit.launcher.ui.model.AppInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

sealed interface LaunchEffect {
    data class FocusPrompt(
        val app: AppInfo,
        val countdownSeconds: Int,
        val usedMinutes: Int? = null,
        val limitMinutes: Int? = null
    ) : LaunchEffect

    data class OpenSettings(val app: AppInfo) : LaunchEffect
}

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    installedApps: InstalledAppsRepository,
    private val decideAppLaunch: DecideAppLaunchUseCase,
    private val updateAppFocus: UpdateAppFocusUseCase,
    observeAppFocusState: ObserveAppFocusStateUseCase
) : ViewModel() {
    val apps: StateFlow<List<AppInfo>> = installedApps.apps

    val focusState: StateFlow<AppFocusState> = observeAppFocusState()

    private val _effects = Channel<LaunchEffect>(capacity = Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onAppClicked(app: AppInfo) {
        when (val decision = decideAppLaunch(app.packageName)) {
            LaunchDecision.LaunchNow -> launch(app.packageName)
            is LaunchDecision.RequireFocusPrompt -> _effects.trySend(
                LaunchEffect.FocusPrompt(app = app, countdownSeconds = decision.countdownSeconds)
            )
            is LaunchDecision.LimitReached -> _effects.trySend(
                LaunchEffect.FocusPrompt(
                    app = app,
                    countdownSeconds = decision.countdownSeconds,
                    usedMinutes = decision.usedMinutes,
                    limitMinutes = decision.limitMinutes
                )
            )
        }
    }

    fun onAppLongPressed(app: AppInfo) {
        if (focusState.value.entryFor(app.packageName).dailyLimitMinutes != null) return
        _effects.trySend(LaunchEffect.OpenSettings(app))
    }

    fun confirmLaunch(packageName: String) = launch(packageName)

    fun setRequirePrompt(packageName: String, enabled: Boolean) {
        updateAppFocus.setRequirePrompt(packageName, enabled)
    }

    fun setDailyLimit(packageName: String, minutes: Int?, days: Int) {
        updateAppFocus.setDailyLimit(packageName, minutes, days)
    }

    private fun launch(packageName: String) {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName) ?: return
        context.startActivity(intent)
    }
}
