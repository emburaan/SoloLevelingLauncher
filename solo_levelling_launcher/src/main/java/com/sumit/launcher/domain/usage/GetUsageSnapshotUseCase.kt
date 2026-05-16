package com.sumit.launcher.domain.usage

import com.sumit.launcher.data.usage.DayUsage
import com.sumit.launcher.data.usage.TodayScreenStats
import com.sumit.launcher.data.usage.UsageStatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

data class UsageSnapshot(
    val days: List<DayUsage>,
    val today: TodayScreenStats
)

/**
 * Combines the 7-day chart and today's screen stats into a single IO query so the
 * ViewModel doesn't have to coordinate two repo calls.
 */
@Singleton
class GetUsageSnapshotUseCase @Inject constructor(
    private val repository: UsageStatsRepository
) {
    fun hasAccess(): Boolean = repository.hasUsageAccess()

    suspend operator fun invoke(): UsageSnapshot = withContext(Dispatchers.IO) {
        UsageSnapshot(
            days = repository.getLastSevenDays(),
            today = repository.getTodayScreenStats()
        )
    }
}
