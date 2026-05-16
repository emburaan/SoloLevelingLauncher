package com.sumit.launcher.ui.presentation.searchscreen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.sumit.launcher.R
import com.sumit.launcher.ui.model.AppInfo
import com.sumit.launcher.ui.presentation.homescreen.component.AppBarIndex

@Composable
fun AppGrid(
    apps: List<AppInfo>,
    onAppClicked: (AppInfo) -> Unit,
    onAppLongPressed: (AppInfo) -> Unit
) {
    val gridState = rememberLazyGridState()
    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Fixed(1),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = dimensionResource(R.dimen.spacing_3xl),
                end = dimensionResource(R.dimen.app_bar_index_padding_end),
                top = dimensionResource(R.dimen.spacing_xs),
                bottom = dimensionResource(R.dimen.spacing_3xl)
            ),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_sm))
        ) {
            items(apps) { app ->
                AppIcon(
                    app = app,
                    onClick = { onAppClicked(app) },
                    onLongClick = { onAppLongPressed(app) }
                )
            }
        }
        AppBarIndex(apps, lazyListState = gridState, this)
    }
}
