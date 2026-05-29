package com.sumit.launcher.ui.presentation.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumit.launcher.data.focus.FocusBlock
import com.sumit.launcher.domain.focus.ManageFocusBlocksUseCase
import com.sumit.launcher.domain.focus.ObserveAppFocusStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FocusBlocksViewModel @Inject constructor(
    observeAppFocusState: ObserveAppFocusStateUseCase,
    private val manageBlocks: ManageFocusBlocksUseCase
) : ViewModel() {
    val blocks: StateFlow<List<FocusBlock>> = observeAppFocusState()
        .map { it.focusBlocks }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun upsert(block: FocusBlock) = manageBlocks.upsert(block)
    fun delete(id: String) = manageBlocks.delete(id)
}
