package com.sumit.launcher.data.apps

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import com.sumit.launcher.data.focus.BlockedApps
import com.sumit.launcher.ui.model.AppInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single source of truth for "what installed apps the launcher will show".
 *
 * Owns the [PackageManager] query, the package-install broadcast subscription, and
 * blocked-package filtering. Icons are decoded once and cached as [ImageBitmap], so
 * the search-grid composable doesn't re-decode on every recomposition.
 */
@Singleton
class InstalledAppsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _apps = MutableStateFlow<List<AppInfo>>(emptyList())
    val apps: StateFlow<List<AppInfo>> = _apps.asStateFlow()

    private val packageChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Ignore the "replacing" half of an app update; the matching
            // ACTION_PACKAGE_ADDED arrives separately and triggers a refresh.
            if (intent?.action == Intent.ACTION_PACKAGE_REMOVED &&
                intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)
            ) return
            refresh()
        }
    }

    init {
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addAction(Intent.ACTION_PACKAGE_CHANGED)
            addDataScheme("package")
        }
        context.registerReceiver(packageChangeReceiver, filter)
        refresh()
    }

    private fun refresh() {
        scope.launch {
            val pm = context.packageManager
            val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val ownPackage = context.packageName
            val list = pm.queryIntentActivities(mainIntent, 0).asSequence()
                .mapNotNull { resolveInfo ->
                    val pkg = resolveInfo.activityInfo.packageName
                    if (pkg == ownPackage || pkg in BlockedApps.PACKAGES) return@mapNotNull null
                    val icon = resolveInfo.loadIcon(pm)
                        .toBitmap(ICON_SIZE_PX, ICON_SIZE_PX)
                        .asImageBitmap()
                    AppInfo(
                        label = resolveInfo.loadLabel(pm).toString(),
                        packageName = pkg,
                        icon = icon
                    )
                }
                .sortedBy { it.label.lowercase() }
                .toList()
            _apps.value = list
        }
    }

    private companion object {
        const val ICON_SIZE_PX = 96
    }
}
