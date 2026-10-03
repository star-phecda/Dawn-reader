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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emptycastle.novery.ui.theme.DawnSiteGold
import com.emptycastle.novery.ui.theme.DawnSitePink
import kotlin.math.abs

private data class ChapterSettleRequest(
    val target: Float,
    val advance: Boolean
)

/**
 * End-of-chapter upward drag.
 *
 * Normal vertical reading remains vertical scrolling until the user is near the chapter end.
 * The gesture starts anywhere in the lower 28% of the page; it is never tied to
 * the visible hint surface or a button edge.
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
    advanceEnabled: Boolean = true,
    content: @Composable () -> Unit
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val widthPx = constraints.maxWidth.toFloat().coerceAtLeast(1f)
        val heightPx = constraints.maxHeight.toFloat().coerceAtLeast(1f)
        val touchSlopPx = 8.dp.value * LocalDensity.current.density
        val thresholdPx = maxOf(84f, heightPx * 0.12f)
        val bottomGestureStartPx = heightPx * 0.68f

        val settleAnimation = remember { Animatable(0f) }
        val haptics = LocalHapticFeedback.current

        var dragOffsetY by remember(chapterKey) { mutableFloatStateOf(0f) }
        var crossedThreshold by remember(chapterKey) { mutableStateOf(false) }
        var settleRequest by remember(chapterKey) {
            mutableStateOf<ChapterSettleRequest?>(null)
        }

        LaunchedEffect(chapterKey) {
            dragOffsetY = 0f
            crossedThreshold = false
            settleRequest = null
            settleAnimation.snapTo(0f)
        }

        LaunchedEffect(settleRequest) {
            val request = settleRequest ?: return@LaunchedEffect
            settleAnimation.snapTo(dragOffsetY)
            settleAnimation.animateTo(
                request.target,
                if (reduceMotion) tween(150) else spring(stiffness = 700f)
            )
            if (request.advance) onNext()
            dragOffsetY = 0f
            crossedThreshold = false
            settleRequest = null
        }

        val renderedOffsetY = settleRequest?.let { settleAnimation.value } ?: dragOffsetY
        val progress = (abs(renderedOffsetY) / thresholdPx).coerceIn(0f, 1f)
        val draggingUp = renderedOffsetY < 0f
        val canAdvance = advanceEnabled && hasNextChapter && allowNextGesture

        Box(modifier = Modifier.fillMaxSize()) {
            if (canAdvance) {
                val isDragging = abs(renderedOffsetY) > 1f

                // Keep the boundary affordance deliberately small. The gesture itself is
                // the navigation control; this pill is only a discoverable hint.
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp)
                        .graphicsLayer {
                            alpha = if (isDragging) 0.96f else 0.72f
                            scaleX = if (isDragging) 1f + (progress * 0.14f) else 1f
                            scaleY = if (isDragging) 1f + (progress * 0.06f) else 1f
                        },
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.92f),
                    border = BorderStroke(
                        1.dp,
                        if (draggingUp && progress >= 1f) {
                            DawnSitePink.copy(alpha = 0.9f)
                        } else {
                            DawnSiteGold.copy(alpha = 0.5f)
                        }
                    ),
                    tonalElevation = 3.dp,
                    shadowElevation = if (isDragging) 6.dp else 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = if (isDragging) 10.dp else 8.dp,
                            vertical = 5.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowUp,
                            contentDescription = "Swipe up for next chapter",
                            tint = if (draggingUp && progress >= 1f) {
                                DawnSitePink
                            } else {
                                DawnSiteGold
                            }
                        )
                        if (isDragging) {
                            Text(
                                text = if (progress >= 1f) {
                                    "Release for next chapter"
                                } else {
                                    "Next chapter"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        translationY = renderedOffsetY
                        val lift = progress * 0.010f
                        scaleX = 1f - lift
                        scaleY = 1f - lift
                        shadowElevation = progress * 14f
                    }
                    .pointerInput(
                        chapterKey,
                        enabled,
                        hasNextChapter,
                        allowNextGesture,
                        advanceEnabled
                    ) {
                        if (!enabled) return@pointerInput

                        awaitEachGesture {
                            val down = awaitFirstDown(requireUnconsumed = false)
                            var lastPosition = down.position
                            var totalDx = 0f
                            var totalDy = 0f
                            var trackingAdvance = false
                            val startsInAdvanceZone = down.position.y >= bottomGestureStartPx

                            while (true) {
                                val event = awaitPointerEvent(PointerEventPass.Initial)
                                val change = event.changes.firstOrNull { it.id == down.id }
                                    ?: event.changes.firstOrNull()
                                    ?: break

                                val delta = change.position - lastPosition
                                lastPosition = change.position
                                totalDx += delta.x
                                totalDy += delta.y

                                if (!trackingAdvance) {
                                    val movedEnough =
                                        abs(totalDx) > touchSlopPx || abs(totalDy) > touchSlopPx
                                    if (movedEnough) {
                                        val verticalIntent = abs(totalDy) > abs(totalDx)
                                        val wantsAdvance = totalDy < 0f
                                        if (
                                            startsInAdvanceZone &&
                                            advanceEnabled &&
                                            hasNextChapter &&
                                            allowNextGesture &&
                                            verticalIntent &&
                                            wantsAdvance
                                        ) {
                                            trackingAdvance = true
                                        } else {
                                            break
                                        }
                                    }
                                }

                                if (trackingAdvance) {
                                    change.consume()
                                    dragOffsetY = (dragOffsetY + delta.y).coerceIn(-heightPx, 0f)
                                    val crossed = abs(dragOffsetY) >= thresholdPx
                                    if (crossed && !crossedThreshold) {
                                        crossedThreshold = true
                                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                    } else if (!crossed) {
                                        crossedThreshold = false
                                    }
                                }

                                if (!change.pressed) break
                            }

                            if (trackingAdvance) {
                                val shouldAdvance =
                                    dragOffsetY <= -thresholdPx &&
                                        advanceEnabled &&
                                        hasNextChapter &&
                                        allowNextGesture

                                settleRequest = ChapterSettleRequest(
                                    target = if (shouldAdvance) -heightPx else 0f,
                                    advance = shouldAdvance
                                )
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
