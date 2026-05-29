package com.sumit.launcher.homescreen.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.drawable.toBitmap
import com.sumit.launcher.R
import com.sumit.launcher.ui.model.AppInfo
import com.sumit.launcher.ui.presentation.searchscreen.component.AppGrid

@Preview
@Composable
fun AppGridComposePreview() {
    val numberOfApps = 30
    val context = LocalContext.current
    val icon = context.getDrawable(R.drawable.sample_icon)
    val appsList: MutableList<AppInfo> = mutableListOf()
    icon?.let {
        val bitmap = it.toBitmap(96, 96).asImageBitmap()
        for (i in 0..numberOfApps) {
            appsList.add(AppInfo("App1$i", "com.example.app$i", bitmap))
        }
    }
    AppGrid(
        apps = appsList,
        onAppClicked = {},
        onAppLongPressed = {}
    )
}
