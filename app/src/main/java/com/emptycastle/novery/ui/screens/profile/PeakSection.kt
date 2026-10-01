package com.emptycastle.novery.ui.screens.profile

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoGraph
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.emptycastle.novery.ui.theme.DawnCoral
import com.emptycastle.novery.ui.theme.DawnGold

@Composable
fun PeakSection(uiState: ProfileUiState, modifier: Modifier = Modifier) {
    var selected by remember { mutableIntStateOf(0) }
    val labels = listOf("TIME", "CHAPTERS", "STREAK")

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.AutoGraph, null, tint = MaterialTheme.colorScheme.primary)
                Text("Your Peaks", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Text("PERSONAL RECORDS", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            labels.forEachIndexed { index, label ->
                FilterChip(selected = selected == index, onClick = { selected = index }, label = { Text(label) })
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            tonalElevation = 3.dp
        ) {
            AnimatedContent(targetState = selected, label = "peak_metric") { metric ->
                val title = when (metric) {
                    0 -> "Longest reading day"
                    1 -> "Most chapters in one day"
                    else -> "Longest streak"
                }
                val value = when (metric) {
                    0 -> formatPeakMinutes(uiState.peakReadingMinutes)
                    1 -> "${uiState.peakChapters}"
                    else -> "${uiState.longestStreak} days"
                }
                val detail = when (metric) {
                    0 -> uiState.peakReadingDate.ifBlank { "No record yet" }
                    1 -> uiState.peakChaptersDate.ifBlank { "No record yet" }
                    else -> if (uiState.longestStreak > 0) "Your best run" else "No record yet"
                }
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = when (metric) {
                            0 -> Icons.Rounded.Schedule
                            1 -> Icons.Rounded.MenuBook
                            else -> Icons.Rounded.LocalFireDepartment
                        },
                        contentDescription = null,
                        tint = if (metric == 2) DawnCoral else DawnGold
                    )
                    Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(value, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

private fun formatPeakMinutes(minutes: Long): String = when {
    minutes >= 60 -> "${minutes / 60}h ${minutes % 60}m"
    else -> "${minutes}m"
}
