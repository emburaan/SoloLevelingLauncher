package com.sumit.sololevelinglauncher.ui.presentation.searchscreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sumit.sololevelinglauncher.ui.presentation.homescreen.component.HomeScreen
import com.sumit.sololevelinglauncher.ui.presentation.homescreen.component.NeumorphicAnalogClock
import com.sumit.sololevelinglauncher.ui.presentation.AppsViewModel
import com.sumit.sololevelinglauncher.ui.theme.SLAccent
import com.sumit.sololevelinglauncher.ui.theme.SLBackground
import com.sumit.sololevelinglauncher.ui.theme.SLPrimary
import com.sumit.sololevelinglauncher.ui.theme.SLText

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