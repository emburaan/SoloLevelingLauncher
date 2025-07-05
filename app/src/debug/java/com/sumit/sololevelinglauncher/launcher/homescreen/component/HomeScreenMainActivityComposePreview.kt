package com.sumit.sololevelinglauncher.launcher.homescreen.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.sumit.sololevelinglauncher.R
import com.sumit.sololevelinglauncher.ui.presentation.searchscreen.component.MainActivityCompose
import com.sumit.sololevelinglauncher.ui.model.AppInfo

@Preview
@Composable
fun HomeScreenMainActivityComposePreview() {
    val numberOfApps = 30
    val context = LocalContext.current
    val icons = context.getDrawable(R.drawable.ic_launcher_background)
    val appsList: MutableList<AppInfo> = mutableListOf()
    icons?.let {
        for (i in 0..numberOfApps) {
            appsList.add(AppInfo("App1$i", "com.example.app$i", it))
        }
    }
    MainActivityCompose()
}