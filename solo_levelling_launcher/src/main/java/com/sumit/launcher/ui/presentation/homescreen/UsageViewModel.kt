package com.sumit.launcher.ui.presentation.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumit.launcher.data.usage.DayUsage
import com.sumit.launcher.data.usage.UsageStatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed interface UsageUiState {
    data object Loading : UsageUiState
    data object NeedsPermission : UsageUiState
    data class Ready(val days: List<DayUsage>) : UsageUiState
}

@HiltViewModel
class UsageViewModel @Inject constructor(
    private val repository: UsageStatsRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UsageUiState>(UsageUiState.Loading)
    val state: StateFlow<UsageUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            if (!repository.hasUsageAccess()) {
                _state.value = UsageUiState.NeedsPermission
                return@launch
            }
            val days = withContext(Dispatchers.IO) { repository.getLastSevenDays() }
            _state.value = UsageUiState.Ready(days)
        }
    }
}
