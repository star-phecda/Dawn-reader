package com.emptycastle.novery.ui.screens.reader.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emptycastle.novery.domain.model.ProgressStyle
import com.emptycastle.novery.ui.screens.reader.theme.ReaderColors
import com.emptycastle.novery.ui.screens.reader.theme.ReaderDefaults
import com.emptycastle.novery.ui.theme.MonoFontFamily
import com.emptycastle.novery.ui.theme.SerifFontFamily

// =============================================================================
// TOP BAR
// =============================================================================

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ReaderTopBar(
    chapterTitle: String,
    chapterNumber: Int,
    totalChapters: Int,
    isBookmarked: Boolean,
    chapterProgress: Float,
    estimatedTimeLeft: String?,
    colors: ReaderColors,
    progressStyle: ProgressStyle = ProgressStyle.BAR,
    largerTouchTargets: Boolean = false,
    onBack: () -> Unit,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues()
    val buttonSize = if (largerTouchTargets) 56.dp else 40.dp
    val displayTimeLeft = estimatedTimeLeft?.takeIf { it.isNotBlank() }
    val progress = chapterProgress.coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 220),
        label = "readerHeaderProgress"
    )
    val titleHidden = chapterTitle.isBlank()

    // Match the bottom toolbar's floating, compact feel without sacrificing reader context.
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        shape = RoundedCornerShape(
            topStart = 26.dp,
            topEnd = 26.dp,
            bottomStart = 26.dp,
            bottomEnd = 14.dp
        ),
        color = colors.controlsBackground,
        border = BorderStroke(1.dp, colors.accent.copy(alpha = 0.10f)),
        shadowElevation = 3.dp,
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(bottom = 4.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = statusBarPadding.calculateTopPadding())
                    .padding(start = 6.dp, end = 6.dp, top = 3.dp, bottom = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(buttonSize),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = colors.accent.copy(alpha = 0.13f),
                        contentColor = colors.accent
                    ),
                    shapes = IconButtonDefaults.shapes(
                        shape = RoundedCornerShape(15.dp),
                        pressedShape = RoundedCornerShape(24.dp)
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go back",
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 2.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = chapterTitle.takeIf { it.isNotBlank() } ?: "DAWN READER",
                        modifier = Modifier.fillMaxWidth(),
                        style = if (titleHidden) {
                            MaterialTheme.typography.labelLarge.copy(
                                fontFamily = MonoFontFamily,
                                letterSpacing = 1.1.sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            MaterialTheme.typography.titleSmall.copy(
                                fontFamily = SerifFontFamily,
                                fontSize = 16.sp,
                                lineHeight = 19.sp,
                                letterSpacing = (-0.2).sp
                            )
                        },
                        fontWeight = if (titleHidden) FontWeight.Bold else FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = if (titleHidden) colors.accent else colors.text
                    )

                    if (progressStyle != ProgressStyle.NONE) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = colors.accent.copy(alpha = 0.07f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ProgressIndicator(
                                    progress = progress,
                                    chapterNumber = chapterNumber,
                                    totalChapters = totalChapters,
                                    estimatedTimeLeft = displayTimeLeft,
                                    style = progressStyle,
                                    colors = colors
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "CHAPTER $chapterNumber",
                            style = MaterialTheme.typography.labelSmall.copy(
                                letterSpacing = 0.8.sp
                            ),
                            color = colors.textSecondary
                        )
                    }
                }

                IconButton(
                    onClick = onBookmarkClick,
                    modifier = Modifier.size(buttonSize),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (isBookmarked) {
                            colors.accent
                        } else {
                            colors.accent.copy(alpha = 0.09f)
                        },
                        contentColor = if (isBookmarked) colors.onAccent else colors.icon
                    ),
                    shapes = IconButtonDefaults.shapes(
                        shape = RoundedCornerShape(15.dp),
                        pressedShape = RoundedCornerShape(24.dp)
                    )
                ) {
                    Icon(
                        imageVector = if (isBookmarked) {
                            Icons.Default.Bookmark
                        } else {
                            Icons.Default.BookmarkBorder
                        },
                        contentDescription = if (isBookmarked) {
                            "Remove bookmark"
                        } else {
                            "Add bookmark"
                        },
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            if (progressStyle == ProgressStyle.BAR) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 14.dp, end = 14.dp, top = 1.dp)
                        .height(2.dp)
                        .clip(RoundedCornerShape(50))
                        .background(colors.progressTrack.copy(alpha = 0.28f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedProgress)
                            .height(2.dp)
                            .clip(RoundedCornerShape(50))
                            .background(colors.accent)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProgressIndicator(
    progress: Float,
    chapterNumber: Int,
    totalChapters: Int,
    estimatedTimeLeft: String?,
    style: ProgressStyle,
    colors: ReaderColors
) {
    when (style) {
        ProgressStyle.NONE -> Unit

        ProgressStyle.BAR -> {
            Text(
                text = "CHAPTER $chapterNumber OF $totalChapters",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = colors.accent
            )
            Text(
                text = "• ${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                color = colors.textSecondary
            )
            if (estimatedTimeLeft != null) {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = colors.textSecondary
                )
                Text(
                    text = estimatedTimeLeft,
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.textSecondary
                )
            }
        }

        ProgressStyle.PERCENTAGE -> {
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = colors.accent
            )
            Text(
                text = "• Ch. $chapterNumber/$totalChapters",
                style = MaterialTheme.typography.labelSmall,
                color = colors.textSecondary
            )
            if (estimatedTimeLeft != null) {
                Text(
                    text = "• $estimatedTimeLeft",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.textSecondary
                )
            }
        }

        ProgressStyle.PAGES -> {
            Text(
                text = "~${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = colors.accent
            )
            Text(
                text = "• Ch. $chapterNumber/$totalChapters",
                style = MaterialTheme.typography.labelSmall,
                color = colors.textSecondary
            )
            if (estimatedTimeLeft != null) {
                Text(
                    text = "• $estimatedTimeLeft left",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.textSecondary
                )
            }
        }

        ProgressStyle.DOTS -> {
            DotsProgressIndicator(
                progress = progress,
                colors = colors
            )
            if (estimatedTimeLeft != null) {
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = estimatedTimeLeft,
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.textSecondary
                )
            }
        }

        ProgressStyle.TIME_LEFT -> {
            Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = colors.accent
            )
            Text(
                text = estimatedTimeLeft ?: "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = colors.accent
            )
            Text(
                text = "• Ch. $chapterNumber",
                style = MaterialTheme.typography.labelSmall,
                color = colors.textSecondary
            )
        }
    }
}

@Composable
private fun DotsProgressIndicator(
    progress: Float,
    colors: ReaderColors,
    dotCount: Int = 5
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(dotCount) { index ->
            val dotProgress = (index + 1).toFloat() / dotCount
            val isActive = progress >= dotProgress
            val isPartial = !isActive && progress > (index.toFloat() / dotCount)

            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isActive -> colors.accent
                            isPartial -> colors.accent.copy(alpha = 0.4f)
                            else -> colors.progressTrack
                        }
                    )
            )
        }
    }
}

// =============================================================================
// TTS ACTIVE INDICATOR
// =============================================================================

@Composable
fun TTSActiveIndicator(
    colors: ReaderColors,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = colors.accent.copy(alpha = 0.15f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            AudioWaveIndicator(color = colors.accent)
            Text(
                text = "Playing",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = colors.accent
            )
        }
    }
}

@Composable
private fun AudioWaveIndicator(
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) { index ->
            val animatedHeight by animateFloatAsState(
                targetValue = when ((System.currentTimeMillis() / 200 + index) % 3) {
                    0L -> 8f
                    1L -> 12f
                    else -> 6f
                },
                animationSpec = tween(150),
                label = "wave_$index"
            )

            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(animatedHeight.dp)
                    .clip(RoundedCornerShape(1.5.dp))
                    .background(color)
            )
        }
    }
}