package com.sumit.launcher.domain.launch

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

/**
 * Combines focus settings + today's usage to decide what happens when a user taps an app:
 * launch it directly, show a focus-prompt countdown, or show a limit-reached countdown.
 */
@Singleton
class DecideAppLaunchUseCase @Inject constructor(
    private val focusRepository: AppFocusRepository,
    private val usageStatsRepository: UsageStatsRepository
) {
    operator fun invoke(packageName: String): LaunchDecision {
        val state = focusRepository.state.value
        val entry = state.entryFor(packageName)

        entry.dailyLimitMinutes?.let { limit ->
            val used = usageStatsRepository.getTodayUsagePerPackage()[packageName] ?: 0
            if (used >= limit) {
                return LaunchDecision.LimitReached(usedMinutes = used, limitMinutes = limit)
            }
        }

        if (entry.requirePrompt) {
            val inFocusBlock = state.activeFocusBlock() != null
            val countdown = if (inFocusBlock) FOCUS_BLOCK_COUNTDOWN else DEFAULT_COUNTDOWN
            return LaunchDecision.RequireFocusPrompt(countdownSeconds = countdown)
        }

        return LaunchDecision.LaunchNow
    }

    private companion object {
        const val DEFAULT_COUNTDOWN = 5
        const val FOCUS_BLOCK_COUNTDOWN = 30
    }
}
