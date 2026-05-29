package com.sumit.launcher.ui.presentation.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumit.launcher.domain.focus.FocusCheckUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FocusCheckViewModel @Inject constructor(
    private val checkFocus: FocusCheckUseCase
) : ViewModel() {
    private val _pendingCheck = MutableStateFlow<Int?>(null)
    val pendingCheck: StateFlow<Int?> = _pendingCheck.asStateFlow()

    /** Called from the home-screen tick. Cheap; safe to call frequently. */
    fun evaluate() {
        if (_pendingCheck.value != null) return // already showing
        viewModelScope.launch {
            val due = withContext(Dispatchers.IO) { checkFocus.checkDue() }
            if (due != null) {
                _pendingCheck.value = due
                checkFocus.markShown()
            }
        }
    }

    fun dismiss() {
        _pendingCheck.value = null
    }
}
