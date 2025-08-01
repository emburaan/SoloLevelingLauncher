package com.sumit.sololevelinglauncher.launcher.presentation.main.component

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.sumit.launcher.ui.presentation.homescreen.component.HomeScreen
import com.sumit.launcher.ui.presentation.searchscreen.component.AppListWithSearchScreen
import com.sumit.launcher.ui.presentation.searchscreen.SearchScreenViewModel
import com.sumit.sololevelinglauncher.ui.theme.SLBackground
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityCompose() {
    val searchScreeViewModel: SearchScreenViewModel = hiltViewModel()
    val apps by searchScreeViewModel.apps.collectAsState()
    val pagerState = rememberPagerState(0, pageCount = { 2 })
    val context = LocalContext.current
    var showSheet by remember { mutableStateOf(true) }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false }
        ) {
            Text(
                "Set as Default Launcher",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                "To get the best experience, set this app as your default launcher.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_MAIN)
                    intent.addCategory(Intent.CATEGORY_HOME)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                    showSheet = false
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Set as Default Launcher")
            }
        }
    }

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