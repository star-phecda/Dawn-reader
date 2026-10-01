package com.emptycastle.novery.ui.screens.details.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.emptycastle.novery.domain.model.NovelDetails
import com.emptycastle.novery.ui.theme.DawnCoral
import com.emptycastle.novery.ui.theme.DawnGold

@Composable
fun NovelInfoDeck(
    details: NovelDetails,
    providerName: String,
    readProgress: Float,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text("NOVEL INFO", style = MaterialTheme.typography.labelSmall, color = DawnCoral, fontWeight = FontWeight.Bold)
                        Text(details.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        Text(
                            listOfNotNull(details.author?.takeIf { it.isNotBlank() }, providerName).joinToString("  •  "),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text("${(readProgress * 100).toInt()}%", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = DawnGold)
                }

                Surface(shape = RoundedCornerShape(99.dp), color = MaterialTheme.colorScheme.surfaceContainerHighest) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(readProgress.coerceIn(0f, 1f)),
                        shape = RoundedCornerShape(99.dp),
                        color = MaterialTheme.colorScheme.primary
                    ) { Spacer(modifier = Modifier.padding(vertical = 4.dp)) }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    InfoStat(Icons.Rounded.AutoStories, "${details.chapters.size}", "chapters", Modifier.weight(1f))
                    details.rating?.let { InfoStat(Icons.Rounded.Star, String.format("%.1f", it / 100f), "rating", Modifier.weight(1f)) }
                    details.views?.let { InfoStat(Icons.Rounded.Visibility, compactNumber(it), "views", Modifier.weight(1f)) }
                }

                if (!details.synopsis.isNullOrBlank()) {
                    Text(details.synopsis!!, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 4, overflow = TextOverflow.Ellipsis)
                }
                if (!details.status.isNullOrBlank()) {
                    Text(details.status!!.uppercase(), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
private fun InfoStat(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String, label: String, modifier: Modifier = Modifier) {
    Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surfaceContainer, modifier = modifier) {
        Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 9.dp), horizontalArrangement = Arrangement.spacedBy(7.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            Column {
                Text(value, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

private fun compactNumber(value: Int): String = when {
    value >= 1_000_000 -> String.format("%.1fM", value / 1_000_000f)
    value >= 1_000 -> String.format("%.1fk", value / 1_000f)
    else -> value.toString()
}
