package com.sumit.sololevelinglauncher.launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sumit.launcher.service.FocusCheckService
import com.sumit.sololevelinglauncher.launcher.presentation.main.component.MainActivityCompose
import com.sumit.launcher.ui.theme.SoloLevelingLauncherTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // The focus-check service isn't restarted on reboot on its own; re-arm it here
        // (this activity is the home screen, so it is created at boot) when enabled.
        FocusCheckService.startIfEnabled(this)
        setContent {
            SoloLevelingLauncherTheme {
                MainActivityCompose()
            }
        }
    }
}
