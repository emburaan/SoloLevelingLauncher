package com.sumit.sololevelinglauncher.ui.presentation.searchscreen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.sumit.sololevelinglauncher.ui.model.AppInfo
import com.sumit.sololevelinglauncher.ui.theme.SLBackground
import com.sumit.sololevelinglauncher.ui.theme.SLText


@Composable
fun AppIcon(app: AppInfo, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .width(50.dp)
            .clickable { onClick() }
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val bitmap = remember(app.icon) { app.icon.toBitmap(64, 64).asImageBitmap() }
        Image(
            bitmap = bitmap,
            contentDescription = app.label,
            modifier = Modifier
                .size(45.dp)
                .background(SLBackground, shape = MaterialTheme.shapes.medium)
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = app.label,
            color = SLText,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}