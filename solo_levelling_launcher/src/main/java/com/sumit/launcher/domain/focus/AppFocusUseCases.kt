package com.sumit.launcher.domain.focus

import com.sumit.launcher.data.focus.AppFocusEntry
import com.sumit.launcher.data.focus.AppFocusRepository
import com.sumit.launcher.data.focus.AppFocusState
import com.sumit.launcher.data.focus.FocusBlock
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveAppFocusStateUseCase @Inject constructor(
    private val repository: AppFocusRepository
) {
    operator fun invoke(): StateFlow<AppFocusState> = repository.state
}

@Singleton
class GetAppFocusEntryUseCase @Inject constructor(
    private val repository: AppFocusRepository
) {
    operator fun invoke(packageName: String): AppFocusEntry =
        repository.state.value.entryFor(packageName)
}

@Singleton
class UpdateAppFocusUseCase @Inject constructor(
    private val repository: AppFocusRepository
) {
    fun setRequirePrompt(packageName: String, enabled: Boolean) {
        repository.setRequirePrompt(packageName, enabled)
    }

    fun setDailyLimit(packageName: String, minutes: Int?) {
        repository.setDailyLimit(packageName, minutes)
    }
}

@Singleton
class ManageFocusBlocksUseCase @Inject constructor(
    private val repository: AppFocusRepository
) {
    fun upsert(block: FocusBlock) = repository.upsertFocusBlock(block)
    fun delete(id: String) = repository.deleteFocusBlock(id)
}
