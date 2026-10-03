package com.emptycastle.novery.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.emptycastle.novery.ui.theme.DawnBlue
import com.emptycastle.novery.ui.theme.DawnCyan
import com.emptycastle.novery.ui.theme.DawnMagenta
import com.emptycastle.novery.ui.theme.DawnViolet
import com.emptycastle.novery.ui.theme.SunGold500

/**
 * Dawn's editorial atmosphere: part creative canvas, part museum wall.
 *
 * Strong negative space, oversized structure and small authored details replace
 * the generic repeating app grid used previously.
 */
@Composable
fun DawnAtmosphere(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val background = MaterialTheme.colorScheme.background
    val ink = MaterialTheme.colorScheme.onBackground

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(background)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val guide = ink.copy(alpha = 0.055f)
            val x1 = size.width * 0.11f
            val x2 = size.width * 0.86f
            val y1 = size.height * 0.23f
            val y2 = size.height * 0.79f

            drawLine(guide, Offset(x1, 0f), Offset(x1, size.height), 1f)
            drawLine(guide, Offset(x2, 0f), Offset(x2, size.height), 1f)
            drawLine(guide, Offset(0f, y1), Offset(size.width, y1), 1f)
            drawLine(guide, Offset(0f, y2), Offset(size.width, y2), 1f)

            drawCircle(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.09f),
                radius = size.minDimension * 0.36f,
                center = Offset(size.width * 0.92f, size.height * 0.12f)
            )
            drawCircle(
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.065f),
                radius = size.minDimension * 0.28f,
                center = Offset(size.width * 0.05f, size.height * 0.78f)
            )
            drawCircle(
                color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.055f),
                radius = size.minDimension * 0.20f,
                center = Offset(size.width * 0.50f, size.height * 0.52f)
            )

            val marks = listOf(
                Offset(size.width * 0.16f, size.height * 0.12f),
                Offset(size.width * 0.74f, size.height * 0.34f),
                Offset(size.width * 0.22f, size.height * 0.67f),
                Offset(size.width * 0.81f, size.height * 0.83f)
            )
            marks.forEachIndexed { index, point ->
                drawCircle(
                    color = when (index % 4) {
                        0 -> DawnCyan.copy(alpha = 0.22f)
                        1 -> DawnMagenta.copy(alpha = 0.20f)
                        2 -> SunGold500.copy(alpha = 0.17f)
                        else -> DawnBlue.copy(alpha = 0.18f)
                    },
                    radius = if (index == 1) 2.2.dp.toPx() else 1.5.dp.toPx(),
                    center = point
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.06f),
                            Color.Transparent
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.04f),
                            Color.Transparent
                        )
                    )
                )
        )

        content()
    }
}
