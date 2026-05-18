package com.sumit.clock.ui.worldclock

import androidx.lifecycle.ViewModel
import com.sumit.clock.worldclock.WorldClockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class WorldClockViewModel @Inject constructor(
    private val repository: WorldClockRepository
) : ViewModel() {
    val zoneIds: StateFlow<List<String>> = repository.zoneIds

    fun add(zoneId: String) = repository.add(zoneId)
    fun remove(zoneId: String) = repository.remove(zoneId)
}
