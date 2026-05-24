package com.sumit.sololevelinglauncher.blocking

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.sumit.launcher.data.focus.BlockedApps
import com.sumit.launcher.data.focus.BlockedDomains

class BlockedAppAccessibilityService : AccessibilityService() {

    private var lastCheckedUrl: String? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        val pkg = event.packageName?.toString() ?: return

        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED &&
            pkg in BlockedApps.PACKAGES
        ) {
            performGlobalAction(GLOBAL_ACTION_HOME)
            return
        }

        val urlBarIds = BROWSER_URL_BAR_IDS[pkg] ?: return
        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED &&
            event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
        ) return

        val root = rootInActiveWindow ?: return
        val url = urlBarIds.firstNotNullOfOrNull { id ->
            root.findAccessibilityNodeInfosByViewId(id).firstOrNull()?.text?.toString()
        }.orEmpty()

        if (url.isEmpty() || url == lastCheckedUrl) return
        lastCheckedUrl = url

        if (BlockedDomains.matches(url)) {
            performGlobalAction(GLOBAL_ACTION_HOME)
        }
    }

    override fun onInterrupt() = Unit

    private companion object {
        val BROWSER_URL_BAR_IDS: Map<String, List<String>> = mapOf(
            "com.android.chrome" to listOf("com.android.chrome:id/url_bar"),
            "com.chrome.beta" to listOf("com.chrome.beta:id/url_bar"),
            "com.chrome.dev" to listOf("com.chrome.dev:id/url_bar"),
            "com.chrome.canary" to listOf("com.chrome.canary:id/url_bar"),
            "com.brave.browser" to listOf("com.brave.browser:id/url_bar"),
            "com.microsoft.emmx" to listOf("com.microsoft.emmx:id/url_bar"),
            "com.opera.browser" to listOf("com.opera.browser:id/url_field"),
            "com.opera.mini.native" to listOf("com.opera.mini.native:id/url_field"),
            "com.sec.android.app.sbrowser" to listOf(
                "com.sec.android.app.sbrowser:id/location_bar_edit_text"
            ),
            "com.duckduckgo.mobile.android" to listOf(
                "com.duckduckgo.mobile.android:id/omnibarTextInput"
            ),
            "org.mozilla.firefox" to listOf(
                "org.mozilla.firefox:id/mozac_browser_toolbar_url_view"
            ),
            "org.mozilla.focus" to listOf(
                "org.mozilla.focus:id/mozac_browser_toolbar_url_view"
            ),
        )
    }
}
