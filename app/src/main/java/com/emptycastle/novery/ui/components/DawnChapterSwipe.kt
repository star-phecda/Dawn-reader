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
import com.emptycastle.novery.ui.theme.DawnCyan
import com.emptycastle.novery.ui.theme.DawnMagenta
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * Dawn's physical chapter gesture.
 *
 * Horizontal intent is handled with Compose's draggable modifier, while the
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
    reduceMotion: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onTap: (Offset, Float, Float) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val widthPx = constraints.maxWidth.toFloat().coerceAtLeast(1f)
        val heightPx = constraints.maxHeight.toFloat().coerceAtLeast(1f)
        val thresholdPx = maxOf(96f, widthPx * 0.20f)
        val touchSlopPx = 8.dp.value * LocalDensity.current.density
        val offsetX = remember { Animatable(0f) }
        val scope = rememberCoroutineScope()
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

        Box(modifier = Modifier.fillMaxSize()) {
            if (direction != 0 && canNavigate) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = if (direction < 0) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Surface(
                        shape = RoundedCornerShape(26.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.94f),
                        border = BorderStroke(1.dp, accent.copy(alpha = 0.65f)),
                        tonalElevation = 3.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 11.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = if (direction < 0) "SWIPE LEFT" else "SWIPE RIGHT",
                                style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.05.sp),
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
                    .fillMaxSize()
                    .graphicsLayer {
                        translationX = offsetX.value
                        val lift = progress * 0.018f
                        scaleX = 1f - lift
                        scaleY = 1f - lift
                        rotationZ = when {
                            offsetX.value < 0f -> -progress * 1.25f
                            offsetX.value > 0f -> progress * 1.25f
                            else -> 0f
                        }
                        shadowElevation = progress * 16f
                    }
                    .pointerInput(chapterKey, enabled, hasPreviousChapter, hasNextChapter) {
                        if (!enabled) return@pointerInput
                        awaitEachGesture {
                            val down = awaitFirstDown(requireUnconsumed = false)
                            var last = down.position
                            var totalDx = 0f
                            var totalDy = 0f
                            var horizontal = false

                            while (true) {
                                val event = awaitPointerEvent()
                                val change = event.changes.firstOrNull { it.id == down.id }
                                    ?: event.changes.firstOrNull()
                                    ?: break
                                val delta = change.position - last
                                last = change.position
                                totalDx += delta.x
                                totalDy += delta.y

                                if (!horizontal &&
                                    (kotlin.math.abs(totalDx) > touchSlopPx ||
                                     kotlin.math.abs(totalDy) > touchSlopPx)
                                ) {
                                    if (kotlin.math.abs(totalDx) > kotlin.math.abs(totalDy)) {
                                        horizontal = true
                                    } else {
                                        break
                                    }
                                }

                                if (horizontal) {
                                    change.consume()
                                    val candidate = (offsetX.value + delta.x).coerceIn(-widthPx, widthPx)
                                    val allowed = when {
                                        candidate < 0f -> hasNextChapter
                                        candidate > 0f -> hasPreviousChapter
                                        else -> true
                                    }
                                    if (allowed) {
                                        scope.launch { offsetX.snapTo(candidate) }
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

                            if (horizontal) {
                                val finalOffset = offsetX.value
                                val goNext = finalOffset <= -thresholdPx && hasNextChapter
                                val goPrevious = finalOffset >= thresholdPx && hasPreviousChapter
                                scope.launch {
                                    if (goNext || goPrevious) {
                                        offsetX.animateTo(
                                            if (goNext) -widthPx else widthPx,
                                            if (reduceMotion) tween(140) else spring(stiffness = 700f)
                                        )
                                        if (goNext) onNext() else onPrevious()
                                    } else {
                                        offsetX.animateTo(
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
