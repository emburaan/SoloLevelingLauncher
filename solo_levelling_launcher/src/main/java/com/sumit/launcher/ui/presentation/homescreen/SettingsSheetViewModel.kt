package com.sumit.launcher.ui.presentation.homescreen

import androidx.lifecycle.ViewModel
import com.sumit.launcher.data.focus.FocusOverlayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsSheetViewModel @Inject constructor(
    private val overlayRepository: FocusOverlayRepository
) : ViewModel() {
    val overlayEnabled: StateFlow<Boolean> = overlayRepository.enabled

    fun setOverlayEnabled(value: Boolean) {
        overlayRepository.setEnabled(value)
    }
}
