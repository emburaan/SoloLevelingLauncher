package com.sumit.launcher.ui.presentation.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumit.launcher.data.usage.DayUsage
import com.sumit.launcher.data.usage.TodayScreenStats
import com.sumit.launcher.domain.usage.GetUsageSnapshotUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface UsageUiState {
    data object Loading : UsageUiState
    data object NeedsPermission : UsageUiState
    data class Ready(
        val days: List<DayUsage>,
        val today: TodayScreenStats
    ) : UsageUiState
}

@HiltViewModel
class UsageViewModel @Inject constructor(
    private val getUsageSnapshot: GetUsageSnapshotUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<UsageUiState>(UsageUiState.Loading)
    val state: StateFlow<UsageUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            if (!getUsageSnapshot.hasAccess()) {
                _state.value = UsageUiState.NeedsPermission
                return@launch
            }
            val snapshot = getUsageSnapshot()
            _state.value = UsageUiState.Ready(days = snapshot.days, today = snapshot.today)
        }
    }
}
