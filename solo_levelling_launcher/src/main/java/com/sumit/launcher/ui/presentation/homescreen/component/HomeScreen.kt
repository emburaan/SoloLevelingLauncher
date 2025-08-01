package com.sumit.launcher.ui.presentation.homescreen.component

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sumit.launcher.R
import com.sumit.todo_list.presentation.component.TaskListSection

@Composable
fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.sung_fundo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Graph at start (top-left)
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 60.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            UsageBarChart(
                usageData = listOf(120, 90, 150, 60, 180, 200, 100),
                modifier = Modifier
                    .height(180.dp)
                    .width(90.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Clock at center
        Box(
            modifier = Modifier
                .padding(top = 70.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            NeumorphicAnalogClock(modifier = Modifier.size(120.dp))
        }

        //Task List at center
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TaskListSection()
                Spacer(modifier = Modifier.height(50.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(25.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    AchievementTile(title = "Physical")
                    AchievementTile(title = "Mental")
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(25.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    AchievementTile(title = "Spiritual")
                    AchievementTile(title = "Accountability")
                }
                Spacer(modifier = Modifier.height(70.dp))
            }
        }
    }
}