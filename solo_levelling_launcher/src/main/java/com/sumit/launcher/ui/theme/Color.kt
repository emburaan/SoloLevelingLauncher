package com.sumit.sololevelinglauncher.ui.theme

import androidx.compose.ui.graphics.Color

// Brand accents — same in both modes
val SLAccentPurple = Color(0xFF6C63FF)
val SLAccentBlue = Color(0xFF42A5F5)
val SLAccentCyan = Color(0xFF22D3EE)

// --- Dark palette (deep navy neumorphic) ---
val DarkBackground = Color(0xFF0F1117)
val DarkSurface = Color(0xFF161924)
val DarkSurfaceHigh = Color(0xFF1D2130)
val DarkSurfaceHigher = Color(0xFF252A3B)
val DarkOnSurface = Color(0xFFE8EAF1)
val DarkOnSurfaceVariant = Color(0xFF9CA0B0)
val DarkShadow = Color(0xFF000000)
val DarkHighlight = Color(0xFF3A3F52)

// --- Light palette (pearl neumorphic) ---
val LightBackground = Color(0xFFE6E9F0)
val LightSurface = Color(0xFFEDF0F6)
val LightSurfaceHigh = Color(0xFFF3F5F9)
val LightSurfaceHigher = Color(0xFFFAFBFD)
val LightOnSurface = Color(0xFF1A1D26)
val LightOnSurfaceVariant = Color(0xFF5C6072)
val LightShadow = Color(0xFFB6BCC9)
val LightHighlight = Color(0xFFFFFFFF)

// Backwards-compat aliases so existing call sites keep compiling. Keep the dark values
// here since the legacy references were used in dark contexts (widget icons, etc.).
val SLBackground = DarkBackground
val SLPrimary = DarkSurfaceHigh
val SLAccent = SLAccentPurple
val SLText = DarkOnSurface
val SLIConBg = DarkSurfaceHigh
