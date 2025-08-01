package com.sumit.launcher.ui.presentation.searchscreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sumit.launcher.ui.model.AppInfo
import com.sumit.launcher.ui.presentation.homescreen.component.AppBarIndex

@Composable
fun AppGrid(apps: List<AppInfo>) {
    val context = LocalContext.current
    val gridState = rememberLazyGridState()
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Fixed(1),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items(apps) { app ->
                        AppIcon(app = app, onClick = {
                            val launchIntent =
                                context.packageManager.getLaunchIntentForPackage(app.packageName)
                            if (launchIntent != null) {
                                context.startActivity(launchIntent)
                            }
                        })
                    }
                }
                AppBarIndex(apps, lazyListState = gridState, this)

            }
        }
    }
}