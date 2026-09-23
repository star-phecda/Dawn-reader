package com.emptycastle.novery.ui.screens.home.tabs.recommendation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.NewReleases
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.emptycastle.novery.recommendation.TagNormalizer
import com.emptycastle.novery.recommendation.model.Recommendation
import com.emptycastle.novery.recommendation.model.RecommendationGroup
import com.emptycastle.novery.recommendation.model.RecommendationType

@Composable
fun RecommendationSection(
    group: RecommendationGroup,
    onNovelClick: (novelUrl: String, providerName: String) -> Unit,
    onNovelLongClick: (Recommendation) -> Unit,
    onQuickDismiss: (Recommendation) -> Unit,
    onSeeAllClick: ((TagNormalizer.TagCategory) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val sectionColor = getSectionColor(group.type)
    val sectionIcon = getSectionIcon(group.type)

    // Extract tag from group title if it's a genre-based recommendation
    val tagCategory = remember(group.title) {
        if (group.title.startsWith("Best in ") || group.title.startsWith("Trending on ")) {
            val tagName = group.title.removePrefix("Best in ").removePrefix("Trending on ")
            // Try to find matching tag category
            TagNormalizer.TagCategory.entries.find {
                TagNormalizer.getDisplayName(it).equals(tagName, ignoreCase = true)
            }
        } else null
    }

    // Only show "See All" if we have both the callback AND a valid tag
    val showSeeAllButton = onSeeAllClick != null &&
            tagCategory != null &&
            group.recommendations.size > 5

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Expressive header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(11.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 5.dp, height = 42.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(sectionColor, sectionColor.copy(alpha = 0.22f))
                            )
                        )
                )
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = group.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    group.subtitle?.let { subtitle ->
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.78f)
                        )
                    }
                }
            }

            if (showSeeAllButton && tagCategory != null) {
                Surface(
                    onClick = { onSeeAllClick?.invoke(tagCategory) },
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHigh
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 11.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = "See all",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }
        }

        // Horizontal scroll of cards
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(
                items = group.recommendations,
                key = { it.novel.url }
            ) { recommendation ->
                RecommendationCard(
                    recommendation = recommendation,
                    onClick = {
                        onNovelClick(
                            recommendation.novel.url,
                            recommendation.novel.apiName
                        )
                    },
                    onLongClick = { onNovelLongClick(recommendation) },
                    onQuickDismiss = { onQuickDismiss(recommendation) }
                )
            }
        }
    }
}

@Composable
private fun getSectionIcon(type: RecommendationType): ImageVector {
    return when (type) {
        RecommendationType.FOR_YOU -> Icons.Rounded.AutoAwesome
        RecommendationType.SIMILAR_TO -> Icons.Rounded.LocalFireDepartment
        RecommendationType.BECAUSE_YOU_READ -> Icons.Rounded.LocalFireDepartment
        RecommendationType.TRENDING_IN_YOUR_GENRES -> Icons.Rounded.TrendingUp
        RecommendationType.FROM_AUTHORS_YOU_LIKE -> Icons.Rounded.Person
        RecommendationType.TOP_RATED_FOR_YOU -> Icons.Rounded.Star
        RecommendationType.NEW_FOR_YOU -> Icons.Rounded.NewReleases
        RecommendationType.DISCOVER_NEW_SOURCE -> Icons.Rounded.Explore
    }
}

@Composable
private fun getSectionColor(type: RecommendationType): Color {
    return when (type) {
        RecommendationType.FOR_YOU -> MaterialTheme.colorScheme.primary
        RecommendationType.SIMILAR_TO -> MaterialTheme.colorScheme.secondary
        RecommendationType.BECAUSE_YOU_READ -> MaterialTheme.colorScheme.secondary
        RecommendationType.TRENDING_IN_YOUR_GENRES -> MaterialTheme.colorScheme.tertiary
        RecommendationType.FROM_AUTHORS_YOU_LIKE -> Color(0xFF9C27B0) // Purple
        RecommendationType.TOP_RATED_FOR_YOU -> Color(0xFFFF9800) // Orange
        RecommendationType.NEW_FOR_YOU -> Color(0xFFE91E63) // Pink
        RecommendationType.DISCOVER_NEW_SOURCE -> Color(0xFF00ACC1) // Cyan
    }
}