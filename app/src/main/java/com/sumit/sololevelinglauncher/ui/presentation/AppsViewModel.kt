package com.sumit.sololevelinglauncher.ui.presentation

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumit.sololevelinglauncher.ui.model.AppInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppsViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _apps = MutableStateFlow<List<AppInfo>>(emptyList())
    val apps: StateFlow<List<AppInfo>> = _apps

    init {
        loadApps()
    }

    private fun loadApps() {
        viewModelScope.launch {
            val packageManager = context.packageManager
            val mainIntent = Intent(Intent.ACTION_MAIN, null)
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            val resolvedApps = packageManager.queryIntentActivities(mainIntent, 0)
            val appList = resolvedApps.asSequence()
                .mapNotNull { resolveInfo ->
                    val label =
                        resolveInfo.loadLabel(packageManager)?.toString() ?: return@mapNotNull null
                    val icon = resolveInfo.loadIcon(packageManager)
                    val packageName = resolveInfo.activityInfo.packageName
                    AppInfo(
                        label = label,
                        packageName = packageName,
                        icon = icon
                    )
                }
                .sortedBy { it.label.lowercase() }
                .toList()
            _apps.value = appList
        }
    }
}
