package com.sumit.sololevelinglauncher.launcher.homescreen

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.sumit.sololevelinglauncher.launcher.homescreen.component.AppGrid
import com.sumit.sololevelinglauncher.launcher.homescreen.component.NeumorphicAnalogClock
import com.sumit.sololevelinglauncher.ui.presentation.AppsViewModel
import com.sumit.sololevelinglauncher.ui.theme.SLBackground
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = SLBackground.toArgb()
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = SLBackground,

                    ) {
                    val appsViewModel: AppsViewModel = hiltViewModel()
                    val apps by appsViewModel.apps.collectAsState()
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
        }
    }
}