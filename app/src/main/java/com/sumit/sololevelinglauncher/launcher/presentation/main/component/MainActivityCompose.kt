package com.sumit.sololevelinglauncher.launcher.presentation.main.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.sumit.launcher.ui.presentation.homescreen.component.HomeScreen
import com.sumit.launcher.ui.presentation.searchscreen.component.AppListWithSearchScreen
import com.sumit.sololevelinglauncher.launcher.presentation.onboarding.OnboardingPrefs
import com.sumit.sololevelinglauncher.launcher.presentation.onboarding.OnboardingScreen

@Composable
fun MainActivityCompose() {
    val context = LocalContext.current
    var onboardingDone by remember { mutableStateOf(OnboardingPrefs.isCompleted(context)) }

    if (!onboardingDone) {
        OnboardingScreen(onComplete = {
            OnboardingPrefs.markCompleted(context)
            onboardingDone = true
        })
        return
    }

    val pagerState = rememberPagerState(0, pageCount = { 2 })
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage == 1 && pagerState.isScrollInProgress }
            .collect { leavingAppList ->
                if (leavingAppList) {
                    focusManager.clearFocus(force = true)
                    keyboardController?.hide()
                }
            }
    }

    BackHandler(enabled = true) { /* Back is disabled on the launcher screens. */ }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> HomeScreen()
                1 -> AppListWithSearchScreen(isCurrentPage = pagerState.settledPage == 1)
            }
        }
    }
}
