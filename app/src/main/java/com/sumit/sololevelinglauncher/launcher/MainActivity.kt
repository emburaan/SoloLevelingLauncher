package com.sumit.sololevelinglauncher.launcher

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import com.sumit.sololevelinglauncher.ui.presentation.AppsViewModel
import com.sumit.sololevelinglauncher.ui.theme.SLBackground
import com.sumit.sololevelinglauncher.ui.theme.SLText
import com.sumit.sololevelinglauncher.ui.theme.SoloLevelingLauncherTheme
import dagger.hilt.android.AndroidEntryPoint

data class AppInfo(
    val label: String,
    val packageName: String,
    val icon: Drawable
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoloLevelingLauncherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = SLBackground
                ) {
                    val appsViewModel: AppsViewModel = hiltViewModel()
                    val apps by appsViewModel.apps.collectAsState()
                    AppGrid(apps = apps)
                }
            }
        }
    }
}

@Composable
fun AppGrid(apps: List<AppInfo>) {
    val context = LocalContext.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier
            .fillMaxSize()
            .background(SLBackground),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        items(apps) { app ->
            AppIcon(app = app, onClick = {
                val launchIntent = context.packageManager.getLaunchIntentForPackage(app.packageName)
                if (launchIntent != null) {
                    context.startActivity(launchIntent)
                }
            })
        }
    }
}

@Composable
fun AppIcon(app: AppInfo, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(72.dp)
            .clickable { onClick() }
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val bitmap = remember(app.icon) { app.icon.toBitmap(64, 64).asImageBitmap() }
        Image(
            bitmap = bitmap,
            contentDescription = app.label,
            modifier = Modifier
                .size(56.dp)
                .background(SLBackground, shape = MaterialTheme.shapes.medium)
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        androidx.compose.material3.Text(
            text = app.label,
            color = SLText,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}