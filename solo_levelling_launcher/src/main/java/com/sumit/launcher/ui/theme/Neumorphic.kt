package com.sumit.sololevelinglauncher.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Soft, elevated card surface in the neumorphic style:
 *  - directional drop shadow tinted with the shadow tone (depth)
 *  - 1 dp inner highlight outline (bevel)
 *  - solid fill with [color]
 *
 * Use on cards, tiles, and any panel that should feel raised off the background.
 */
@Composable
fun Modifier.neumorphicSurface(
    shape: Shape = RoundedCornerShape(20.dp),
    elevation: Dp = 12.dp,
    color: Color = MaterialTheme.colorScheme.surfaceContainer,
    highlightAlpha: Float = 0.18f
): Modifier {
    val tones = SLTheme.tones
    return this
        .shadow(
            elevation = elevation,
            shape = shape,
            ambientColor = tones.shadow,
            spotColor = tones.shadow,
            clip = false
        )
        .clip(shape)
        .background(color)
        .border(1.dp, tones.highlight.copy(alpha = highlightAlpha), shape)
}

/** Same as [neumorphicSurface] but with a brand gradient fill instead of a solid color. */
@Composable
fun Modifier.neumorphicAccentSurface(
    shape: Shape = RoundedCornerShape(20.dp),
    elevation: Dp = 14.dp,
    colors: List<Color> = listOf(SLAccentPurple, SLAccentBlue)
): Modifier {
    val tones = SLTheme.tones
    return this
        .shadow(
            elevation = elevation,
            shape = shape,
            ambientColor = tones.accentGlow,
            spotColor = tones.accentGlow,
            clip = false
        )
        .clip(shape)
        .background(Brush.linearGradient(colors))
}
