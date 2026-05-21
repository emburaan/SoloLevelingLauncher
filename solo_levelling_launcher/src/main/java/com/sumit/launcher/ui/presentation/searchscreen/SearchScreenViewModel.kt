package com.sumit.launcher.ui.presentation.searchscreen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumit.launcher.data.focus.AppFocusState
import com.sumit.launcher.domain.focus.ObserveAppFocusStateUseCase
import com.sumit.launcher.domain.focus.UpdateAppFocusUseCase
import com.sumit.launcher.domain.launch.DecideAppLaunchUseCase
import com.sumit.launcher.domain.launch.LaunchDecision
import com.sumit.launcher.ui.model.AppInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
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
    private val decideAppLaunch: DecideAppLaunchUseCase,
    private val updateAppFocus: UpdateAppFocusUseCase,
    observeAppFocusState: ObserveAppFocusStateUseCase
) : ViewModel() {
    private val _apps = MutableStateFlow<List<AppInfo>>(emptyList())
    val apps: StateFlow<List<AppInfo>> = _apps

    val focusState: StateFlow<AppFocusState> = observeAppFocusState()

    private val _effects = Channel<LaunchEffect>(capacity = Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    private val packageChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Ignore the "replacing" half of an app update; the matching
            // ACTION_PACKAGE_ADDED / ACTION_PACKAGE_REMOVED arrives separately.
            if (intent?.getBooleanExtra(Intent.EXTRA_REPLACING, false) == true &&
                intent.action == Intent.ACTION_PACKAGE_REMOVED
            ) return
            loadApps()
        }
    }

    init {
        loadApps()
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addAction(Intent.ACTION_PACKAGE_CHANGED)
            addDataScheme("package")
        }
        context.registerReceiver(packageChangeReceiver, filter)
    }

    override fun onCleared() {
        super.onCleared()
        runCatching { context.unregisterReceiver(packageChangeReceiver) }
    }

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
        _effects.trySend(LaunchEffect.OpenSettings(app))
    }

    fun confirmLaunch(packageName: String) = launch(packageName)

    fun setRequirePrompt(packageName: String, enabled: Boolean) {
        updateAppFocus.setRequirePrompt(packageName, enabled)
    }

    fun setDailyLimit(packageName: String, minutes: Int?) {
        updateAppFocus.setDailyLimit(packageName, minutes)
    }

    private fun launch(packageName: String) {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName) ?: return
        context.startActivity(intent)
    }

    private fun loadApps() {
        viewModelScope.launch {
            val packageManager = context.packageManager
            val mainIntent = Intent(Intent.ACTION_MAIN, null)
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            val resolvedApps = packageManager.queryIntentActivities(mainIntent, 0)
            val ownPackage = context.packageName
            val appList = resolvedApps.asSequence()
                .mapNotNull { resolveInfo ->
                    val packageName = resolveInfo.activityInfo.packageName
                    if (packageName == ownPackage) return@mapNotNull null
                    val label = resolveInfo.loadLabel(packageManager).toString()
                    val icon = resolveInfo.loadIcon(packageManager)
                    AppInfo(
                        label = label,
                        packageName = packageName,
                        icon = icon
                    )
                }
                .sortedBy { it.label.lowercase() }
                .toList()
            _apps.value = appList
        }
    }
}
