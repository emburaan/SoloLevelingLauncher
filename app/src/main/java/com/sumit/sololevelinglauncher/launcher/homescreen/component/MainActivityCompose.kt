package com.sumit.sololevelinglauncher.launcher.homescreen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sumit.sololevelinglauncher.ui.model.AppInfo
import com.sumit.sololevelinglauncher.ui.presentation.AppsViewModel
import com.sumit.sololevelinglauncher.ui.theme.SLBackground

@Composable
fun MainActivityCompose() {
    val appsViewModel: AppsViewModel = hiltViewModel()
    val apps by appsViewModel.apps.collectAsState()
    MainActivityBodyCompose(apps)
}

@Composable
fun MainActivityBodyCompose(apps: List<AppInfo>) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = SLBackground,

        ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            NeumorphicAnalogClock()
            AppGrid(apps = apps)
        }
    }
}