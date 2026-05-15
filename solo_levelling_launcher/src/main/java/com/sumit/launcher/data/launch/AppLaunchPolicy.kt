package com.sumit.launcher.data.launch

import com.sumit.launcher.data.focus.AppFocusRepository
import com.sumit.launcher.data.usage.UsageStatsRepository
import javax.inject.Inject
import javax.inject.Singleton

sealed interface LaunchDecision {
    object LaunchNow : LaunchDecision
    data class RequireFocusPrompt(val countdownSeconds: Int = 5) : LaunchDecision
    data class LimitReached(
        val usedMinutes: Int,
        val limitMinutes: Int,
        val countdownSeconds: Int = 15
    ) : LaunchDecision
}

@Singleton
class AppLaunchPolicy @Inject constructor(
    private val focusRepository: AppFocusRepository,
    private val usageStatsRepository: UsageStatsRepository
) {
    fun decide(packageName: String): LaunchDecision {
        val entry = focusRepository.state.value.entryFor(packageName)
        val limit = entry.dailyLimitMinutes
        if (limit != null) {
            val used = usageStatsRepository.getTodayUsagePerPackage()[packageName] ?: 0
            if (used >= limit) {
                return LaunchDecision.LimitReached(usedMinutes = used, limitMinutes = limit)
            }
        }
        if (entry.requirePrompt) return LaunchDecision.RequireFocusPrompt()
        return LaunchDecision.LaunchNow
    }
}
