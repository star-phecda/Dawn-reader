package com.emptycastle.novery.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emptycastle.novery.ui.theme.DawnSiteGold
import com.emptycastle.novery.ui.theme.DawnSitePink
import kotlin.math.abs

/**
 * Dawn's horizontal chapter gesture.
 *
 * A deliberate horizontal drag from anywhere in the reader moves the current
 * chapter with the finger. Vertical movement is left alone for normal reading
 * scroll. Crossing the threshold gives haptic feedback; releasing commits the
 * chapter change or springs the page back into place.
 */
@Composable
fun DawnChapterSwipeSurface(
    chapterKey: String,
    enabled: Boolean,
    hasPreviousChapter: Boolean,
    hasNextChapter: Boolean,
    allowPreviousGesture: Boolean = true,
    allowNextGesture: Boolean = true,
    reduceMotion: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onTap: (Offset, Float, Float) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        val widthPx = constraints.maxWidth.toFloat().coerceAtLeast(1f)
        val heightPx = constraints.maxHeight.toFloat().coerceAtLeast(1f)
        val touchSlopPx = 8.dp.value * LocalDensity.current.density
        val thresholdPx = maxOf(96f, widthPx * 0.24f)
        val offsetX = remember { Animatable(0f) }
        val haptics = LocalHapticFeedback.current
        var crossedThreshold by remember(chapterKey) { mutableStateOf(false) }

        LaunchedEffect(chapterKey) {
            offsetX.snapTo(0f)
            crossedThreshold = false
        }

        val progress = (abs(offsetX.value) / thresholdPx).coerceIn(0f, 1f)
        val direction = when {
            offsetX.value < 0f -> -1
            offsetX.value > 0f -> 1
            else -> 0
        }
        val canNavigate = when {
            direction < 0 -> hasNextChapter && allowNextGesture
            direction > 0 -> hasPreviousChapter && allowPreviousGesture
            else -> false
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (direction != 0 && canNavigate) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = if (direction < 0) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Surface(
                        shape = RoundedCornerShape(26.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.97f),
                        border = BorderStroke(
                            1.dp,
                            if (direction < 0) DawnSitePink.copy(alpha = 0.68f)
                            else DawnSiteGold.copy(alpha = 0.68f)
                        ),
                        tonalElevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 11.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = when {
                                    direction < 0 && progress >= 1f -> "RELEASE TO CONTINUE"
                                    direction > 0 && progress >= 1f -> "RELEASE TO RETURN"
                                    direction < 0 -> "NEXT CHAPTER"
                                    else -> "PREVIOUS CHAPTER"
                                },
                                style = MaterialTheme.typography.labelMedium.copy(
                                    letterSpacing = 1.05.sp
                                ),
                                color = if (direction < 0) DawnSitePink else DawnSiteGold
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        translationX = offsetX.value
                        val lift = progress * 0.012f
                        scaleX = 1f - lift
                        scaleY = 1f - lift
                        rotationZ = when {
                            offsetX.value < 0f -> -progress * 1.1f
                            offsetX.value > 0f -> progress * 1.1f
                            else -> 0f
                        }
                        shadowElevation = progress * 14f
                    }
                    .pointerInput(
                        chapterKey,
                        enabled,
                        hasPreviousChapter,
                        hasNextChapter,
                        allowPreviousGesture,
                        allowNextGesture
                    ) {
                        if (!enabled) return@pointerInput

                        awaitEachGesture {
                            val down = awaitFirstDown(requireUnconsumed = false)
                            var lastPosition = down.position
                            var totalDx = 0f
                            var totalDy = 0f
                            var trackingHorizontal = false
                            var sawVerticalIntent = false

                                while (true) {
                                    val event = awaitPointerEvent()
                                    val change = event.changes.firstOrNull { it.id == down.id }
                                        ?: event.changes.firstOrNull()
                                        ?: break

                                    val delta = change.position - lastPosition
                                    lastPosition = change.position
                                    totalDx += delta.x
                                    totalDy += delta.y

                                    if (!trackingHorizontal && !sawVerticalIntent) {
                                        val movedEnough = abs(totalDx) > touchSlopPx ||
                                            abs(totalDy) > touchSlopPx

                                        if (movedEnough) {
                                            if (abs(totalDx) > abs(totalDy)) {
                                                val wantsNext = totalDx < 0f
                                                val allowed = if (wantsNext) {
                                                    hasNextChapter && allowNextGesture
                                                } else {
                                                    hasPreviousChapter && allowPreviousGesture
                                                }

                                                if (allowed) {
                                                    trackingHorizontal = true
                                                } else {
                                                    break
                                                }
                                            } else {
                                                sawVerticalIntent = true
                                            }
                                        }
                                    }

                                    if (trackingHorizontal) {
                                        change.consume()
                                        val candidate = (offsetX.value + delta.x)
                                            .coerceIn(-widthPx, widthPx)

                                        val allowed = when {
                                            candidate < 0f -> hasNextChapter && allowNextGesture
                                            candidate > 0f -> hasPreviousChapter && allowPreviousGesture
                                            else -> true
                                        }

                                        if (allowed) {
                                            offsetX.snapTo(candidate)
                                            val crossed = abs(candidate) >= thresholdPx
                                            if (crossed && !crossedThreshold) {
                                                crossedThreshold = true
                                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                            } else if (!crossed) {
                                                crossedThreshold = false
                                            }
                                        }
                                    }

                                    if (!change.pressed) break
                                }

                                if (trackingHorizontal) {
                                    val finalOffset = offsetX.value
                                    val goNext = finalOffset <= -thresholdPx &&
                                        hasNextChapter && allowNextGesture
                                    val goPrevious = finalOffset >= thresholdPx &&
                                        hasPreviousChapter && allowPreviousGesture

                                    if (goNext || goPrevious) {
                                        offsetX.animateTo(
                                            if (goNext) -widthPx else widthPx,
                                            if (reduceMotion) tween(150) else spring(stiffness = 700f)
                                        )
                                        if (goNext) onNext() else onPrevious()
                                        offsetX.snapTo(0f)
                                    } else {
                                        offsetX.animateTo(
                                            0f,
                                            if (reduceMotion) tween(120) else spring(stiffness = 850f)
                                        )
                                    }

                                    crossedThreshold = false
                                } else if (
                                    abs(totalDx) <= touchSlopPx &&
                                    abs(totalDy) <= touchSlopPx
                                ) {
                                    onTap(down.position, widthPx, heightPx)
                                }
                        }
                    }
            ) {
                content()
            }
        }
    }
}
