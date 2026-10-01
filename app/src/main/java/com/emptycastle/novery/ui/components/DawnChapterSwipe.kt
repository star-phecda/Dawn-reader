package com.emptycastle.novery.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.emptycastle.novery.ui.theme.DawnSiteCream
import com.emptycastle.novery.ui.theme.DawnSiteGold
import com.emptycastle.novery.ui.theme.DawnSiteInk
import com.emptycastle.novery.ui.theme.DawnSitePink
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * Dawn's physical chapter gesture.
 *
 * Vertical chapter intent is handled as a continuous drag, while the
 * ReaderContainer underneath remains a normal vertical LazyColumn. This is
 * deliberately a drag, not a simple swipe detector: the chapter follows the
 * finger continuously and springs back when the threshold is not reached.
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
    edgeZoneRatio: Float = 0.22f,
    content: @Composable () -> Unit
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val widthPx = constraints.maxWidth.toFloat().coerceAtLeast(1f)
        val heightPx = constraints.maxHeight.toFloat().coerceAtLeast(1f)
        val thresholdPx = maxOf(110f, heightPx * 0.18f)
        val touchSlopPx = 8.dp.value * LocalDensity.current.density
        val edgeZonePx = heightPx * edgeZoneRatio.coerceIn(0.12f, 0.35f)
        val offsetY = remember { Animatable(0f) }
        val scope = rememberCoroutineScope()
        val haptics = LocalHapticFeedback.current
        var crossedThreshold by remember(chapterKey) { mutableStateOf(false) }

        LaunchedEffect(chapterKey) {
            offsetY.snapTo(0f)
            crossedThreshold = false
        }

        val progress = (kotlin.math.abs(offsetY.value) / thresholdPx).coerceIn(0f, 1f)
        val direction = when {
            offsetY.value < 0f -> -1
            offsetY.value > 0f -> 1
            else -> 0
        }
        val canNavigate = when {
            direction < 0 -> hasNextChapter && allowNextGesture
            direction > 0 -> hasPreviousChapter && allowPreviousGesture
            else -> false
        }
        val accent = if (direction < 0) DawnSitePink else DawnSiteGold

        Box(modifier = Modifier.fillMaxSize()) {
            if (direction != 0 && canNavigate) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = if (direction < 0) Alignment.BottomCenter else Alignment.TopCenter
                ) {
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.97f),
                        border = BorderStroke(1.dp, accent.copy(alpha = 0.62f)),
                        tonalElevation = 5.dp
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
                                    direction < 0 -> "SWIPE UP FROM EDGE"
                                    else -> "SWIPE DOWN FROM EDGE"
                                },
                                style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.05.sp),
                                color = accent
                            )
                            Text(
                                text = if (direction < 0) "NEXT CHAPTER  ↑" else "PREVIOUS CHAPTER  ↓",
                                style = MaterialTheme.typography.titleLarge,
                                color = if (MaterialTheme.colorScheme.isLight) DawnSiteInk else DawnSiteCream
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        translationY = offsetY.value
                        val lift = progress * 0.018f
                        scaleX = 1f - lift
                        scaleY = 1f - lift
                        rotationZ = when {
                            offsetY.value < 0f -> -progress * 1.25f
                            offsetY.value > 0f -> progress * 1.25f
                            else -> 0f
                        }
                        shadowElevation = progress * 16f
                    }
                    .pointerInput(chapterKey, enabled, hasPreviousChapter, hasNextChapter, allowPreviousGesture, allowNextGesture) {
                        if (!enabled) return@pointerInput
                        awaitEachGesture {
                            val down = awaitFirstDown(requireUnconsumed = false)
                            var last = down.position
                            var totalDx = 0f
                            var totalDy = 0f
                            var vertical = false
                            var trackingEdgeGesture = false
                            val startedAtTopEdge = down.position.y <= edgeZonePx
                            val startedAtBottomEdge = down.position.y >= heightPx - edgeZonePx

                            while (true) {
                                val event = awaitPointerEvent()
                                val change = event.changes.firstOrNull { it.id == down.id }
                                    ?: event.changes.firstOrNull()
                                    ?: break
                                val delta = change.position - last
                                last = change.position
                                totalDx += delta.x
                                totalDy += delta.y

                                if (!vertical &&
                                    (kotlin.math.abs(totalDx) > touchSlopPx ||
                                     kotlin.math.abs(totalDy) > touchSlopPx)
                                ) {
                                    if (kotlin.math.abs(totalDy) > kotlin.math.abs(totalDx)) {
                                        vertical = true
                                        trackingEdgeGesture = when {
                                            totalDy < 0f -> startedAtBottomEdge && hasNextChapter && allowNextGesture
                                            totalDy > 0f -> startedAtTopEdge && hasPreviousChapter && allowPreviousGesture
                                            else -> false
                                        }
                                    } else {
                                        break
                                    }
                                }

                                if (vertical && trackingEdgeGesture) {
                                    change.consume()
                                    val candidate = (offsetY.value + delta.y).coerceIn(-heightPx, heightPx)
                                    val allowed = when {
                                        candidate < 0f -> hasNextChapter && allowNextGesture
                                        candidate > 0f -> hasPreviousChapter && allowPreviousGesture
                                        else -> true
                                    }
                                    if (allowed) {
                                        scope.launch { offsetY.snapTo(candidate) }
                                        val crossed = kotlin.math.abs(candidate) >= thresholdPx
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

                            if (vertical && trackingEdgeGesture) {
                                val finalOffset = offsetY.value
                                val goNext = finalOffset <= -thresholdPx && hasNextChapter && allowNextGesture
                                val goPrevious = finalOffset >= thresholdPx && hasPreviousChapter && allowPreviousGesture
                                scope.launch {
                                    if (goNext || goPrevious) {
                                        offsetY.animateTo(
                                            if (goNext) -heightPx else heightPx,
                                            if (reduceMotion) tween(140) else spring(stiffness = 700f)
                                        )
                                        if (goNext) onNext() else onPrevious()
                                    } else {
                                        offsetY.animateTo(
                                            0f,
                                            if (reduceMotion) tween(110) else spring(stiffness = 850f)
                                        )
                                    }
                                    crossedThreshold = false
                                }
                            } else if (
                                kotlin.math.abs(totalDx) <= touchSlopPx &&
                                kotlin.math.abs(totalDy) <= touchSlopPx
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
