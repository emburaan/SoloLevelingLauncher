package com.sumit.sololevelinglauncher.ui.presentation

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumit.sololevelinglauncher.launcher.AppInfo
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
            val intent = Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val resolveInfos = packageManager.queryIntentActivities(intent, 0)
            val appList = resolveInfos.asSequence()
                .map {
                    AppInfo(
                        label = it.loadLabel(packageManager).toString(),
                        packageName = it.activityInfo.packageName,
                        icon = it.loadIcon(packageManager)
                    )
                }
                .sortedBy { it.label.lowercase() }
                .toList()
            _apps.value = appList
        }
    }
}
