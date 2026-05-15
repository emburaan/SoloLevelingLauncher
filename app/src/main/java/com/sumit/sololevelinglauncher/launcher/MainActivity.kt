package com.sumit.sololevelinglauncher.launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sumit.sololevelinglauncher.launcher.presentation.main.component.MainActivityCompose
import com.sumit.sololevelinglauncher.ui.theme.SoloLevelingLauncherTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SoloLevelingLauncherTheme {
                MainActivityCompose()
            }
        }
    }
}
