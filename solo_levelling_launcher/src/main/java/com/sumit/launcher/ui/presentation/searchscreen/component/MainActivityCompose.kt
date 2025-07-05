package com.sumit.sololevelinglauncher.ui.presentation.searchscreen.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.sumit.launcher.ui.presentation.homescreen.component.HomeScreen
import com.sumit.launcher.ui.presentation.searchscreen.component.AppListWithSearchScreen
import com.sumit.sololevelinglauncher.ui.presentation.AppsViewModel
import com.sumit.sololevelinglauncher.ui.theme.SLBackground

@Composable
fun MainActivityCompose() {
    val appsViewModel: AppsViewModel = hiltViewModel()
    val apps by appsViewModel.apps.collectAsState()
    val pagerState = rememberPagerState(0, pageCount = { 2 })
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = SLBackground,
    ) {
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> {
                    HomeScreen()
                }

                1 -> {
                    AppListWithSearchScreen(apps)
                }
            }
        }
    }
}