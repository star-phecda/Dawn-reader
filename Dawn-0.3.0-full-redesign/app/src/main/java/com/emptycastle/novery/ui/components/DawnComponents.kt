package com.emptycastle.novery.ui.components

import androidx.compose.animation.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emptycastle.novery.ui.theme.DawnBlue
import com.emptycastle.novery.ui.theme.DawnCyan
import com.emptycastle.novery.ui.theme.DawnMagenta
import com.emptycastle.novery.ui.theme.DawnNavy
import com.emptycastle.novery.ui.theme.DawnViolet

@Composable
fun DawnGradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        color = Color.Transparent,
        shadowElevation = 8.dp,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(listOf(DawnCyan, DawnBlue, DawnMagenta))
                )
                .padding(horizontal = 18.dp, vertical = 13.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                it()
                Spacer(Modifier.size(8.dp))
            }
            Text(
                text = text,
                color = DawnNavy,
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp)
            )
        }
    }
}

@Composable
fun DawnSectionHeader(
    title: String,
    subtitle: String? = null,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(34.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        Brush.verticalGradient(listOf(DawnCyan, DawnViolet, DawnMagenta))
                    )
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                subtitle?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
        if (actionLabel != null && onAction != null) {
            Surface(
                onClick = onAction,
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh
            ) {
                Text(
                    text = actionLabel,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 13.dp, vertical = 9.dp)
                )
            }
        }
    }
}

@Composable
fun DawnPageHeader(
    eyebrow: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(
                        Brush.verticalGradient(listOf(DawnCyan, DawnBlue, DawnMagenta))
                    )
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = eyebrow.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = DawnCyan,
                    letterSpacing = 1.7.sp
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 17.dp, top = 7.dp, end = 4.dp)
        )
    }
}

@Composable
fun DawnHeroSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        color = Color.Transparent,
        shadowElevation = 8.dp,
        tonalElevation = 2.dp
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            DawnCyan.copy(alpha = 0.18f),
                            DawnBlue.copy(alpha = 0.09f),
                            DawnViolet.copy(alpha = 0.12f),
                            DawnMagenta.copy(alpha = 0.15f)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            content()
        }
    }
}

@Composable
fun DawnPill(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val brush = if (selected) {
        Brush.horizontalGradient(listOf(DawnCyan.copy(alpha = 0.92f), DawnViolet.copy(alpha = 0.9f)))
    } else null

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = if (selected) Color.Transparent else MaterialTheme.colorScheme.surfaceContainerHigh,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (selected) DawnCyan.copy(alpha = 0.7f) else MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Box(
            modifier = Modifier
                .then(if (brush != null) Modifier.background(brush) else Modifier)
                .padding(horizontal = 15.dp, vertical = 9.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = if (selected) DawnNavy else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun DawnIconTile(
    modifier: Modifier = Modifier,
    tint: Color = DawnCyan,
    icon: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(tint.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
    ) { icon() }
}

/**
 * Small, deliberate end-of-chapter gesture affordance.
 * It only exists at the chapter boundary, so ordinary reading gestures remain untouched.
 */
@Composable
fun DawnChapterAdvancePull(
    visible: Boolean,
    hasNextChapter: Boolean,
    onTrigger: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!visible || !hasNextChapter) return

    val haptics = LocalHapticFeedback.current
    val threshold = 110f
    var dragDistance by remember { mutableFloatStateOf(0f) }
    var crossed by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = (dragDistance / threshold).coerceIn(0f, 1.35f),
        animationSpec = spring(stiffness = 650f),
        label = "chapter_pull_progress"
    )
    val ready = progress >= 1f

    Box(
        modifier = modifier
            .padding(bottom = 14.dp)
            .size(width = 172.dp, height = 74.dp)
            .graphicsLayer {
                translationY = -28f * progress.coerceAtMost(1f)
                alpha = 0.76f + (progress.coerceIn(0f, 1f) * 0.24f)
            }
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onVerticalDrag = { _, dragAmount ->
                        if (dragAmount < 0f) {
                            dragDistance = (dragDistance - dragAmount).coerceIn(0f, 150f)
                            val nowCrossed = dragDistance >= threshold
                            if (nowCrossed && !crossed) {
                                crossed = true
                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            }
                        } else {
                            dragDistance = (dragDistance - dragAmount * 0.35f).coerceAtLeast(0f)
                        }
                    },
                    onDragEnd = {
                        if (dragDistance >= threshold) onTrigger()
                        dragDistance = 0f
                        crossed = false
                    },
                    onDragCancel = {
                        dragDistance = 0f
                        crossed = false
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.94f),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (ready) DawnCyan.copy(alpha = 0.8f) else MaterialTheme.colorScheme.outlineVariant
            ),
            tonalElevation = 5.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (ready) DawnCyan.copy(alpha = 0.18f) else DawnViolet.copy(alpha = 0.13f),
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(38.dp)) {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowUp,
                            contentDescription = null,
                            tint = if (ready) DawnCyan else DawnViolet,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    Text(
                        text = if (ready) "Release for next" else "Next chapter",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (ready) "Let go to continue" else "Pull upward",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
