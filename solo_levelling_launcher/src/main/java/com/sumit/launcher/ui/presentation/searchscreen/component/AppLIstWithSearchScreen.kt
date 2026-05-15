package com.sumit.launcher.ui.presentation.searchscreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sumit.launcher.ui.model.AppInfo
import com.sumit.launcher.ui.presentation.searchscreen.LaunchEffect
import com.sumit.launcher.ui.presentation.searchscreen.SearchScreenViewModel

@Composable
fun AppListWithSearchScreen(
    viewModel: SearchScreenViewModel = hiltViewModel()
) {
    val apps by viewModel.apps.collectAsState()
    val focusState by viewModel.focusState.collectAsState()
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    var prompt by remember { mutableStateOf<LaunchEffect.FocusPrompt?>(null) }
    var settingsApp by remember { mutableStateOf<AppInfo?>(null) }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is LaunchEffect.FocusPrompt -> prompt = effect
                is LaunchEffect.OpenSettings -> settingsApp = effect.app
            }
        }
    }

    val filteredApps = if (searchQuery.text.isBlank()) {
        apps
    } else {
        apps.filter { it.label.contains(searchQuery.text, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface
                    )
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
            placeholder = {
                Text("Search apps", color = MaterialTheme.colorScheme.onSurfaceVariant)
            },
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            trailingIcon = {
                if (searchQuery.text.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = TextFieldValue("") }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Clear",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(56.dp)
                .fillMaxHeight()
        )
        Spacer(modifier = Modifier.height(16.dp))
        AppGrid(
            apps = filteredApps,
            onAppClicked = viewModel::onAppClicked,
            onAppLongPressed = viewModel::onAppLongPressed
        )
    }

    prompt?.let { effect ->
        FocusPromptDialog(
            appLabel = effect.app.label,
            countdownSeconds = effect.countdownSeconds,
            usedMinutes = effect.usedMinutes,
            limitMinutes = effect.limitMinutes,
            onConfirm = {
                viewModel.confirmLaunch(effect.app.packageName)
                prompt = null
            },
            onDismiss = { prompt = null }
        )
    }

    settingsApp?.let { app ->
        AppFocusSettingsSheet(
            app = app,
            entry = focusState.entryFor(app.packageName),
            onSave = { requirePrompt, dailyLimitMinutes ->
                viewModel.setRequirePrompt(app.packageName, requirePrompt)
                viewModel.setDailyLimit(app.packageName, dailyLimitMinutes)
                settingsApp = null
            },
            onDismiss = { settingsApp = null }
        )
    }
}
