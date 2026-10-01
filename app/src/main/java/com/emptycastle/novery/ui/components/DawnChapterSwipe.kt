package com.emptycastle.novery.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.matchParentSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import com.emptycastle.novery.ui.theme.DawnCyan
import com.emptycastle.novery.ui.theme.DawnMagenta

/**
 * Physical horizontal chapter transition for Dawn.
 *
 * Horizontal dragging moves the current chapter with the finger. The adjacent
 * direction is revealed underneath. Vertical movement is deliberately ignored
 * so ordinary LazyColumn reading scroll remains natural.
 */
@Composable
fun DawnChapterSwipeSurface(
    chapterKey: String,
    enabled: Boolean,
    hasPreviousChapter: Boolean,
    hasNextChapter: Boolean,
    reduceMotion: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val widthPx = constraints.maxWidth.toFloat().coerceAtLeast(1f)
        val thresholdPx = maxOf(96f, widthPx * 0.22f)
        val offsetX = remember { Animatable(0f) }
        val haptics = LocalHapticFeedback.current
        var crossedThreshold by remember(chapterKey) { mutableStateOf(false) }

        LaunchedEffect(chapterKey) {
            offsetX.snapTo(0f)
            crossedThreshold = false
        }

        val progress = (kotlin.math.abs(offsetX.value) / thresholdPx).coerceIn(0f, 1f)
        val direction = when {
            offsetX.value < 0f -> -1
            offsetX.value > 0f -> 1
            else -> 0
        }
        val canNavigate = when {
            direction < 0 -> hasNextChapter
            direction > 0 -> hasPreviousChapter
            else -> false
        }
        val accent = if (direction < 0) DawnCyan else DawnMagenta

        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                contentAlignment = if (direction < 0) Alignment.CenterEnd else Alignment.CenterStart
            ) {
                if (direction != 0 && canNavigate) {
                    Surface(
                        shape = RoundedCornerShape(28.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.95f),
                        border = BorderStroke(1.dp, accent.copy(alpha = 0.72f)),
                        tonalElevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 13.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = if (direction < 0) "NEXT" else "PREVIOUS",
                                style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 1.15.sp),
                                color = accent
                            )
                            Text(
                                text = if (direction < 0) "→" else "←",
                                style = MaterialTheme.typography.titleLarge,
                                color = accent
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        translationX = offsetX.value
                        val lift = progress * 0.018f
                        scaleX = 1f - lift
                        scaleY = 1f - lift
                        rotationZ = when {
                            offsetX.value < 0f -> -progress * 1.5f
                            offsetX.value > 0f -> progress * 1.5f
                            else -> 0f
                        }
                        shadowElevation = progress * 16f
                    }
                    .pointerInput(enabled, chapterKey, hasPreviousChapter, hasNextChapter) {
                        if (!enabled) return@pointerInput

                        coroutineScope {
                            detectHorizontalDragGestures(
                                onHorizontalDrag = { change, dragAmount ->
                                    val candidate = (offsetX.value + dragAmount).coerceIn(-widthPx, widthPx)
                                    val allowed = when {
                                        candidate < 0f -> hasNextChapter
                                        candidate > 0f -> hasPreviousChapter
                                        else -> true
                                    }

                                    change.consume()

                                    if (allowed) {
                                        launch { offsetX.snapTo(candidate) }

                                        val nowCrossed = kotlin.math.abs(candidate) >= thresholdPx
                                        if (nowCrossed && !crossedThreshold) {
                                            crossedThreshold = true
                                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                        } else if (!nowCrossed) {
                                            crossedThreshold = false
                                        }
                                    }
                                },
                                onDragEnd = {
                                    val finalOffset = offsetX.value
                                    val goNext = finalOffset <= -thresholdPx && hasNextChapter
                                    val goPrevious = finalOffset >= thresholdPx && hasPreviousChapter

                                    launch {
                                        if (goNext || goPrevious) {
                                            offsetX.animateTo(
                                                targetValue = if (goNext) -widthPx else widthPx,
                                                animationSpec = if (reduceMotion) tween(140) else spring(stiffness = 700f)
                                            )
                                            if (goNext) onNext() else onPrevious()
                                        } else {
                                            offsetX.animateTo(
                                                targetValue = 0f,
                                                animationSpec = if (reduceMotion) tween(110) else spring(stiffness = 850f)
                                            )
                                        }
                                        crossedThreshold = false
                                    }
                                },
                                onDragCancel = {
                                    launch {
                                        offsetX.animateTo(
                                            targetValue = 0f,
                                            animationSpec = if (reduceMotion) tween(110) else spring(stiffness = 850f)
                                        )
                                        crossedThreshold = false
                                    }
                                }
                            )
                        }
                    }
            ) {
                content()
            }
        }
    }
}
