package com.sumit.launcher.ui.presentation.homescreen.component

import android.view.MotionEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sumit.launcher.ui.model.AppInfo
import kotlinx.coroutines.launch

@Composable
fun AppBarIndex(
    apps: List<AppInfo>,
    lazyListState: LazyGridState,
    boxScope: BoxScope
) {
    val alphabets = apps.map { it.label.firstOrNull()?.uppercaseChar() ?: '#' }
        .distinct()
        .sorted()

    val alphabetToIndex = remember(apps) {
        alphabets.associateWith { letter ->
            apps.indexOfFirst { it.label.firstOrNull()?.uppercaseChar() == letter }
        }
    }

    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    var pressedAlphabet by remember { mutableStateOf<Char?>(null) }
    var showBubble by remember { mutableStateOf(false) }
    var bubbleOffsetYPx by remember { mutableStateOf(0f) }
    var indexBarHeightPx by remember { mutableStateOf(1f) }
    var indexBarTopY by remember { mutableStateOf(0f) }
    var lastChar by remember { mutableStateOf<Char?>(null) }

    val bubbleAlpha by animateFloatAsState(
        targetValue = if (showBubble) 1f else 0f,
        animationSpec = tween(200), label = "bubbleAlpha"
    )

    val bubbleScale by animateFloatAsState(
        targetValue = if (showBubble) 1f else 0.7f,
        animationSpec = tween(200), label = "bubbleScale"
    )

    with(boxScope) {
        // BUBBLE
        if (showBubble && pressedAlphabet != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset {
                        IntOffset(
                            x = (-60).dp.roundToPx(),
                            y = bubbleOffsetYPx.toInt()
                        )
                    }
                    .graphicsLayer {
                        alpha = bubbleAlpha
                        scaleX = bubbleScale
                        scaleY = bubbleScale
                    }
                    .size(48.dp)
                    .background(Color.White, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = pressedAlphabet.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }
        }

        // INDEX BAR
        Column(
            modifier = Modifier
                .width(50.dp)
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
                .padding(vertical = 8.dp)
                .onGloballyPositioned { layout ->
                    indexBarHeightPx = layout.size.height.toFloat()
                    indexBarTopY = layout.positionInWindow().y
                }
                .pointerInteropFilter { event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                            val y = event.rawY - indexBarTopY
                            val itemHeight = indexBarHeightPx / alphabets.size
                            val index = (y / itemHeight).toInt().coerceIn(0, alphabets.size - 1)
                            val char = alphabets[index]

                            if (lastChar != char) {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                lastChar = char
                            }

                            pressedAlphabet = char
                            showBubble = true
                            bubbleOffsetYPx = y

                            val scrollIndex = alphabetToIndex[char] ?: 0
                            coroutineScope.launch {
                                lazyListState.scrollToItem(scrollIndex)
                            }
                        }

                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                            pressedAlphabet = null
                            showBubble = false
                            lastChar = null
                        }
                    }
                    true
                },
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            alphabets.forEach { alphabet ->
                Box(
                    modifier = Modifier.size(28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = alphabet.toString(),
                        fontSize = 14.sp,
                        color = if (pressedAlphabet == alphabet) Color.Black else Color.White,
                        fontWeight = if (pressedAlphabet == alphabet) FontWeight.Bold else FontWeight.Normal
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
            }
        }
    }
}







