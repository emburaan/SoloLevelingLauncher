package com.sumit.launcher.data.focus

/**
 * Built-in list of package names that should be flagged for the focus prompt
 * by default — social media + entertainment apps. The user can still explicitly
 * disable any of these via the long-press settings sheet; that adds the package
 * to `disabledDefaults` and overrides the heuristic.
 */
internal val DEFAULT_DISTRACTOR_PACKAGES: Set<String> = setOf(
    // Social
    "com.instagram.android",
    "com.instagram.lite",
    "com.facebook.katana",
    "com.facebook.lite",
    "com.twitter.android",
    "com.zhiliaoapp.musically",       // TikTok (global)
    "com.ss.android.ugc.trill",       // TikTok Lite
    "com.ss.android.ugc.aweme",       // Douyin
    "com.snapchat.android",
    "com.reddit.frontpage",
    "com.linkedin.android",
    "com.pinterest",
    "com.instagram.threadsapp",       // Threads
    "com.discord",
    "com.bsky.app",                   // Bluesky
    // Video / entertainment
    "com.google.android.youtube",
    "com.google.android.apps.youtube.music",
    "com.netflix.mediaclient",
    "com.amazon.avod.thirdpartyclient",
    "com.disney.disneyplus",
    "com.hotstar.android",
    "com.spotify.music",
    "com.hulu.plus",
    "tv.twitch.android.app",
    "com.crunchyroll.crunchyroid",
)
