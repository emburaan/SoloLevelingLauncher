package com.sumit.launcher.homescreen.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.sumit.launcher.R
import com.sumit.launcher.ui.presentation.searchscreen.component.AppGrid
import com.sumit.launcher.ui.model.AppInfo

@Preview
@Composable
fun AppGridComposePreview() {
    val numberOfApps = 30
    val context = LocalContext.current
    val icons = context.getDrawable(R.drawable.sample_icon)
    val appsList: MutableList<AppInfo> = mutableListOf()
    icons?.let {
        for (i in 0..numberOfApps) {
            appsList.add(AppInfo("App1$i", "com.example.app$i", it))
        }
    }
    AppGrid(
        apps = appsList,
        onAppClicked = {},
        onAppLongPressed = {}
    )
}