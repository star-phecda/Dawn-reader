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
import androidx.compose.ui.text.style.TextAlign
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
    val buttonSize = if (largerTouchTargets) 56.dp else 44.dp
    val displayTimeLeft = estimatedTimeLeft?.takeIf { it.isNotBlank() }
    val progress = chapterProgress.coerceIn(0f, 1f)

    // Keep the expressive asymmetrical silhouette, but give the page back its space.
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        shape = RoundedCornerShape(
            topStart = 26.dp,
            topEnd = 26.dp,
            bottomStart = 26.dp,
            bottomEnd = 12.dp
        ),
        color = colors.controlsBackground,
        shadowElevation = 4.dp,
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(bottom = 6.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = statusBarPadding.calculateTopPadding())
                    .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(buttonSize),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = colors.accent.copy(alpha = 0.14f),
                        contentColor = colors.accent
                    ),
                    shapes = IconButtonDefaults.shapes(
                        shape = RoundedCornerShape(16.dp),
                        pressedShape = RoundedCornerShape(8.dp)
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go back"
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "DAWN",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontFamily = MonoFontFamily,
                            letterSpacing = 1.5.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = colors.accent
                    )
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .clip(CircleShape)
                            .background(colors.accent)
                    )
                    Text(
                        text = "READER",
                        style = MaterialTheme.typography.labelSmall.copy(
                            letterSpacing = 1.1.sp
                        ),
                        color = colors.textSecondary
                    )
                }

                IconButton(
                    onClick = onBookmarkClick,
                    modifier = Modifier.size(buttonSize),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (isBookmarked) {
                            colors.accent
                        } else {
                            colors.accent.copy(alpha = 0.10f)
                        },
                        contentColor = if (isBookmarked) colors.onAccent else colors.icon
                    ),
                    shapes = IconButtonDefaults.shapes(
                        shape = RoundedCornerShape(16.dp),
                        pressedShape = RoundedCornerShape(8.dp)
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
                        }
                    )
                }
            }

            if (chapterTitle.isNotBlank()) {
                Text(
                    text = chapterTitle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = SerifFontFamily,
                        fontSize = 20.sp,
                        lineHeight = 24.sp,
                        letterSpacing = (-0.3).sp
                    ),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                    color = colors.text
                )
            }

            if (progressStyle != ProgressStyle.NONE) {
                Surface(
                    modifier = Modifier.padding(
                        start = 14.dp,
                        end = 14.dp,
                        top = 4.dp,
                        bottom = if (progressStyle == ProgressStyle.BAR) 0.dp else 2.dp
                    ),
                    shape = RoundedCornerShape(50),
                    color = colors.accent.copy(alpha = 0.08f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
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
            }

            if (progressStyle == ProgressStyle.BAR) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 14.dp, end = 14.dp, top = 5.dp)
                        .height(3.dp)
                        .clip(RoundedCornerShape(50))
                        .background(colors.progressTrack.copy(alpha = 0.28f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .height(3.dp)
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
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = colors.accent
            )
            Text(
                text = "• ${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelMedium,
                color = colors.textSecondary
            )
            if (estimatedTimeLeft != null) {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = colors.textSecondary
                )
                Text(
                    text = estimatedTimeLeft,
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.textSecondary
                )
            }
        }

        ProgressStyle.PERCENTAGE -> {
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = colors.accent
            )
            Text(
                text = "• Ch. $chapterNumber/$totalChapters",
                style = MaterialTheme.typography.labelMedium,
                color = colors.textSecondary
            )
            if (estimatedTimeLeft != null) {
                Text(
                    text = "• $estimatedTimeLeft",
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.textSecondary
                )
            }
        }

        ProgressStyle.PAGES -> {
            Text(
                text = "~${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = colors.accent
            )
            Text(
                text = "• Ch. $chapterNumber/$totalChapters",
                style = MaterialTheme.typography.labelMedium,
                color = colors.textSecondary
            )
            if (estimatedTimeLeft != null) {
                Text(
                    text = "• $estimatedTimeLeft left",
                    style = MaterialTheme.typography.labelMedium,
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
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.textSecondary
                )
            }
        }

        ProgressStyle.TIME_LEFT -> {
            Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = colors.accent
            )
            Text(
                text = estimatedTimeLeft ?: "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = colors.accent
            )
            Text(
                text = "• Ch. $chapterNumber",
                style = MaterialTheme.typography.labelMedium,
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