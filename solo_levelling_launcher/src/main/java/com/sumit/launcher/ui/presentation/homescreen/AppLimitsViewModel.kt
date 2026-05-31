package com.sumit.launcher.ui.presentation.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumit.launcher.data.apps.InstalledAppsRepository
import com.sumit.launcher.data.focus.AppFocusState
import com.sumit.launcher.domain.focus.ObserveAppFocusStateUseCase
import com.sumit.launcher.domain.focus.UpdateAppFocusUseCase
import com.sumit.launcher.ui.model.AppInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppLimitsViewModel @Inject constructor(
    installedApps: InstalledAppsRepository,
    observeAppFocusState: ObserveAppFocusStateUseCase,
    private val updateAppFocus: UpdateAppFocusUseCase
) : ViewModel() {
    val focusState: StateFlow<AppFocusState> = observeAppFocusState()

    /** Installed apps that currently have an active (non-expired) daily limit. */
    val limitedApps: StateFlow<List<AppInfo>> =
        combine(installedApps.apps, focusState) { apps, state ->
            apps.filter { state.entryFor(it.packageName).dailyLimitMinutes != null }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), emptyList())

    fun setRequirePrompt(packageName: String, enabled: Boolean) {
        updateAppFocus.setRequirePrompt(packageName, enabled)
    }

    fun setDailyLimit(packageName: String, minutes: Int?, days: Int) {
        updateAppFocus.setDailyLimit(packageName, minutes, days)
    }
}
