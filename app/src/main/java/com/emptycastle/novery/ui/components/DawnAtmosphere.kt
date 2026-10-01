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
import com.emptycastle.novery.ui.theme.DawnCyan
import com.emptycastle.novery.ui.theme.DawnMagenta
import com.emptycastle.novery.ui.theme.DawnViolet
import com.emptycastle.novery.ui.theme.SunGold500

/**
 * Shared Dawn atmosphere inspired by Finn's living canvas and Phecda's museum grid.
 *
 * The texture stays deliberately subtle so covers, titles, and reader content remain dominant.
 */
@Composable
fun DawnAtmosphere(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val gridColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.035f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val step = 32.dp.toPx()

            var x = 0f
            while (x <= size.width) {
                drawLine(
                    color = gridColor,
                    start = Offset(x, 0f),
                    end = Offset(x, size.height),
                    strokeWidth = 1f
                )
                x += step
            }

            var y = 0f
            while (y <= size.height) {
                drawLine(
                    color = gridColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1f
                )
                y += step
            }

            val stars = listOf(
                Offset(size.width * 0.10f, size.height * 0.16f),
                Offset(size.width * 0.18f, size.height * 0.72f),
                Offset(size.width * 0.34f, size.height * 0.28f),
                Offset(size.width * 0.61f, size.height * 0.12f),
                Offset(size.width * 0.78f, size.height * 0.34f),
                Offset(size.width * 0.90f, size.height * 0.70f),
                Offset(size.width * 0.48f, size.height * 0.84f)
            )

            stars.forEachIndexed { index, point ->
                drawCircle(
                    color = when (index % 4) {
                        0 -> DawnCyan.copy(alpha = 0.20f)
                        1 -> DawnMagenta.copy(alpha = 0.17f)
                        2 -> SunGold500.copy(alpha = 0.15f)
                        else -> DawnViolet.copy(alpha = 0.17f)
                    },
                    radius = 1.5.dp.toPx(),
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
                            DawnCyan.copy(alpha = 0.11f),
                            Color.Transparent
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            DawnMagenta.copy(alpha = 0.075f),
                            Color.Transparent
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            SunGold500.copy(alpha = 0.035f),
                            Color.Transparent
                        )
                    )
                )
        )

        content()
    }
}