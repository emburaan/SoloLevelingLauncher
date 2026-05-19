package com.sumit.sololevelinglauncher.launcher.presentation.onboarding

import android.Manifest
import android.app.AlarmManager
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.BatteryChargingFull
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.sumit.sololevelinglauncher.R
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(onComplete: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    // Force re-evaluation of grant predicates when the user returns from a settings screen.
    var resumeTick by remember { mutableIntStateOf(0) }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) resumeTick++
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val pages = remember(resumeTick) { buildPages(context) }

    // Nothing left to ask for — bail straight into the launcher.
    LaunchedEffect(pages.isEmpty()) {
        if (pages.isEmpty()) onComplete()
    }
    if (pages.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { pages.size })

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* re-evaluation happens via ON_RESUME */ }

    val roleLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { /* re-evaluation happens via ON_RESUME */ }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
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
                )
                .padding(
                    top = OnboardingDefaults.ScreenPaddingTop,
                    bottom = OnboardingDefaults.ScreenPaddingBottom
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = OnboardingDefaults.SkipRowHorizontalPadding),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onComplete) {
                    Text(
                        text = stringRes(context, R.string.onboarding_skip_all),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    horizontal = OnboardingDefaults.ContentHorizontalPadding
                )
            ) { index ->
                OnboardingPage(page = pages[index])
            }

            PageIndicator(
                pageCount = pages.size,
                currentPage = pagerState.currentPage,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = OnboardingDefaults.IndicatorVerticalPadding)
            )

            val page = pages[pagerState.currentPage]
            val granted by remember(page, resumeTick) {
                derivedStateOf { page.isGranted(context) }
            }
            val isLast = pagerState.currentPage == pages.lastIndex

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = OnboardingDefaults.ContentHorizontalPadding)
            ) {
                Button(
                    onClick = {
                        when {
                            granted && !isLast -> {
                                scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                            }
                            granted && isLast -> onComplete()
                            else -> page.action(
                                context,
                                {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        notificationPermissionLauncher.launch(
                                            Manifest.permission.POST_NOTIFICATIONS
                                        )
                                    }
                                },
                                {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                        val roleManager = context.getSystemService(RoleManager::class.java)
                                        if (roleManager != null
                                            && roleManager.isRoleAvailable(RoleManager.ROLE_HOME)
                                            && !roleManager.isRoleHeld(RoleManager.ROLE_HOME)
                                        ) {
                                            roleLauncher.launch(
                                                roleManager.createRequestRoleIntent(RoleManager.ROLE_HOME)
                                            )
                                            return@action
                                        }
                                    }
                                    context.startActivity(
                                        Intent(Settings.ACTION_HOME_SETTINGS)
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    )
                                }
                            )
                        }
                    },
                    shape = RoundedCornerShape(OnboardingDefaults.ButtonCornerRadius),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(OnboardingDefaults.ButtonHeight)
                ) {
                    val label = when {
                        granted && isLast -> stringRes(context, R.string.onboarding_get_started)
                        granted -> stringRes(context, R.string.onboarding_continue)
                        else -> stringRes(context, page.actionLabelRes)
                    }
                    Text(
                        text = label,
                        fontSize = OnboardingDefaults.ButtonLabelFontSize,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(OnboardingDefaults.ButtonRowGap))
                TextButton(
                    onClick = {
                        if (isLast) onComplete()
                        else scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringRes(
                            context,
                            if (isLast) R.string.onboarding_finish else R.string.onboarding_maybe_later
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun OnboardingPage(page: OnboardingPage) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = OnboardingDefaults.PageHorizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IllustrationTile(icon = page.icon)
        Spacer(modifier = Modifier.height(OnboardingDefaults.IllustrationToTitleSpacing))
        Text(
            text = stringRes(context, page.titleRes),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(OnboardingDefaults.TitleToBulletsSpacing))
        page.bulletRes.forEach { bullet ->
            BulletRow(text = stringRes(context, bullet))
            Spacer(modifier = Modifier.height(OnboardingDefaults.BulletRowSpacing))
        }
    }
}

@Composable
private fun IllustrationTile(icon: ImageVector) {
    Box(
        modifier = Modifier
            .size(OnboardingDefaults.IllustrationTileSize)
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(
                            alpha = OnboardingDefaults.IllustrationContainerAlpha
                        ),
                        MaterialTheme.colorScheme.surfaceContainerHighest
                    )
                ),
                shape = RoundedCornerShape(OnboardingDefaults.IllustrationCornerRadius)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(OnboardingDefaults.IllustrationIconSize)
        )
    }
}

@Composable
private fun BulletRow(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = OnboardingDefaults.BulletRowHorizontalPadding),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Outlined.Check,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(top = OnboardingDefaults.BulletIconTopPadding)
                .size(OnboardingDefaults.BulletIconSize)
        )
        Spacer(modifier = Modifier.width(OnboardingDefaults.BulletIconTextSpacing))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(OnboardingDefaults.IndicatorDotSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { i ->
            val isActive = i == currentPage
            AnimatedContent(
                targetState = isActive,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "indicator-$i"
            ) { active ->
                Box(
                    modifier = Modifier
                        .height(OnboardingDefaults.IndicatorDotSize)
                        .width(
                            if (active) OnboardingDefaults.IndicatorActiveDotWidth
                            else OnboardingDefaults.IndicatorDotSize
                        )
                        .background(
                            color = if (active)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                    alpha = OnboardingDefaults.IndicatorInactiveAlpha
                                ),
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

private fun stringRes(context: Context, @androidx.annotation.StringRes id: Int): String =
    context.getString(id)

// ────────────────────────── pages ──────────────────────────

private data class OnboardingPage(
    val titleRes: Int,
    val bulletRes: List<Int>,
    val actionLabelRes: Int,
    val icon: ImageVector,
    val isGranted: (Context) -> Boolean,
    val action: (
        context: Context,
        requestNotifications: () -> Unit,
        requestLauncherRole: () -> Unit
    ) -> Unit
)

private fun buildPages(context: Context): List<OnboardingPage> {
    val pages = mutableListOf<OnboardingPage>()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !notificationsGranted(context)) {
        pages += OnboardingPage(
            titleRes = R.string.onboarding_notifications_title,
            bulletRes = listOf(
                R.string.onboarding_notifications_b1,
                R.string.onboarding_notifications_b2,
                R.string.onboarding_notifications_b3
            ),
            actionLabelRes = R.string.dialog_allow,
            icon = Icons.Outlined.Notifications,
            isGranted = ::notificationsGranted,
            action = { _, requestNotifications, _ -> requestNotifications() }
        )
    }

    if (!hasUsageAccess(context)) {
        pages += OnboardingPage(
            titleRes = R.string.onboarding_usage_title,
            bulletRes = listOf(
                R.string.onboarding_usage_b1,
                R.string.onboarding_usage_b2,
                R.string.onboarding_usage_b3
            ),
            actionLabelRes = R.string.dialog_open_settings,
            icon = Icons.Outlined.Insights,
            isGranted = ::hasUsageAccess,
            action = { ctx, _, _ ->
                ctx.startActivity(
                    Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        )
    }

    if (!isIgnoringBatteryOptimizations(context)) {
        pages += OnboardingPage(
            titleRes = R.string.onboarding_battery_title,
            bulletRes = listOf(
                R.string.onboarding_battery_b1,
                R.string.onboarding_battery_b2,
                R.string.onboarding_battery_b3
            ),
            actionLabelRes = R.string.dialog_battery_allow,
            icon = Icons.Outlined.BatteryChargingFull,
            isGranted = ::isIgnoringBatteryOptimizations,
            action = { ctx, _, _ ->
                ctx.startActivity(
                    Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                        .setData(Uri.parse("package:${ctx.packageName}"))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        )
    }

    if (!Settings.canDrawOverlays(context)) {
        pages += OnboardingPage(
            titleRes = R.string.onboarding_overlay_title,
            bulletRes = listOf(
                R.string.onboarding_overlay_b1,
                R.string.onboarding_overlay_b2,
                R.string.onboarding_overlay_b3
            ),
            actionLabelRes = R.string.dialog_open_settings,
            icon = Icons.Outlined.Layers,
            isGranted = { Settings.canDrawOverlays(it) },
            action = { ctx, _, _ ->
                ctx.startActivity(
                    Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:${ctx.packageName}")
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        )
    }

    if (needsExactAlarm(context)) {
        pages += OnboardingPage(
            titleRes = R.string.onboarding_exact_alarm_title,
            bulletRes = listOf(
                R.string.onboarding_exact_alarm_b1,
                R.string.onboarding_exact_alarm_b2,
                R.string.onboarding_exact_alarm_b3
            ),
            actionLabelRes = R.string.dialog_open_settings,
            icon = Icons.Outlined.Alarm,
            isGranted = { !needsExactAlarm(it) },
            action = { ctx, _, _ ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ctx.startActivity(
                        Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                            .setData(Uri.parse("package:${ctx.packageName}"))
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                }
            }
        )
    }

    if (!isDefaultLauncher(context)) {
        pages += OnboardingPage(
            titleRes = R.string.onboarding_launcher_title,
            bulletRes = listOf(
                R.string.onboarding_launcher_b1,
                R.string.onboarding_launcher_b2,
                R.string.onboarding_launcher_b3
            ),
            actionLabelRes = R.string.onboarding_launcher_action,
            icon = Icons.Outlined.Home,
            isGranted = ::isDefaultLauncher,
            action = { _, _, requestLauncherRole -> requestLauncherRole() }
        )
    }

    return pages
}

// ────────────────────────── predicates ──────────────────────────

private fun notificationsGranted(context: Context): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
    return context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
}

private fun hasUsageAccess(context: Context): Boolean {
    val appOps = context.getSystemService(Context.APP_OPS_SERVICE)
        as? android.app.AppOpsManager ?: return false
    val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        appOps.unsafeCheckOpNoThrow(
            android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            context.packageName
        )
    } else {
        @Suppress("DEPRECATION")
        appOps.checkOpNoThrow(
            android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            context.packageName
        )
    }
    return mode == android.app.AppOpsManager.MODE_ALLOWED
}

private fun isIgnoringBatteryOptimizations(context: Context): Boolean {
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as? PowerManager
        ?: return true
    return powerManager.isIgnoringBatteryOptimizations(context.packageName)
}

private fun needsExactAlarm(context: Context): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return false
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) return false
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        ?: return false
    return !alarmManager.canScheduleExactAlarms()
}

private fun isDefaultLauncher(context: Context): Boolean {
    val resolveInfo = context.packageManager.resolveActivity(
        Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME),
        PackageManager.MATCH_DEFAULT_ONLY
    )
    return resolveInfo?.activityInfo?.packageName == context.packageName
}

// ────────────────────────── completion flag ──────────────────────────

object OnboardingPrefs {
    private const val PREFS = "onboarding"
    private const val KEY_COMPLETED = "completed"

    fun isCompleted(context: Context): Boolean =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getBoolean(KEY_COMPLETED, false)

    fun markCompleted(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY_COMPLETED, true).apply()
    }
}
