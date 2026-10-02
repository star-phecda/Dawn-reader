package com.emptycastle.novery.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.emptycastle.novery.domain.model.UiDensity

/**
 * Density-aware dimension values used throughout the app
 */
@Immutable
data class NoveryDimensions(
    // Bottom Navigation
    val bottomBarIconSize: Dp,
    val showBottomBarLabels: Boolean,
    val bottomBarHeight: Dp,

    // Grid & Cards
    val cardSpacing: Dp,
    val gridPadding: Dp,
    val cardCornerRadius: Dp,

    // General spacing
    val spacingXs: Dp,
    val spacingSm: Dp,
    val spacingMd: Dp,
    val spacingLg: Dp,
    val spacingXl: Dp,

    // Icon sizes
    val iconSm: Dp,
    val iconMd: Dp,
    val iconLg: Dp,

    // Touch targets
    val minTouchTarget: Dp
) {
    companion object {
        fun fromDensity(density: UiDensity): NoveryDimensions {
            return when (density) {
                UiDensity.COMPACT -> NoveryDimensions(
                    bottomBarIconSize = 20.dp,
                    showBottomBarLabels = false,
                    bottomBarHeight = 64.dp,
                    cardSpacing = 6.dp,
                    gridPadding = 10.dp,
                    cardCornerRadius = 14.dp,
                    spacingXs = 4.dp,
                    spacingSm = 6.dp,
                    spacingMd = 8.dp,
                    spacingLg = 12.dp,
                    spacingXl = 18.dp
                    iconSm = 16.dp,
                    iconMd = 20.dp,
                    iconLg = 24.dp,
                    minTouchTarget = 40.dp
                )
                UiDensity.DEFAULT -> NoveryDimensions(
                    bottomBarIconSize = 24.dp,
                    showBottomBarLabels = true,
                    bottomBarHeight = 72.dp,
                    cardSpacing = 8.dp,
                    gridPadding = 12.dp,
                    cardCornerRadius = 18.dp,
                    spacingXs = 4.dp,
                    spacingSm = 8.dp,
                    spacingMd = 10.dp,
                    spacingLg = 16.dp,
                    spacingXl = 24.dp
                    iconSm = 18.dp,
                    iconMd = 24.dp,
                    iconLg = 28.dp,
                    minTouchTarget = 48.dp
                )
                UiDensity.COMFORTABLE -> NoveryDimensions(
                    bottomBarIconSize = 28.dp,
                    showBottomBarLabels = true,
                    bottomBarHeight = 76.dp,
                    cardSpacing = 10.dp,
                    gridPadding = 16.dp,
                    cardCornerRadius = 22.dp,
                    spacingXs = 4.dp,
                    spacingSm = 8.dp,
                    spacingMd = 12.dp,
                    spacingLg = 20.dp,
                    spacingXl = 32.dp
                    iconSm = 20.dp,
                    iconMd = 28.dp,
                    iconLg = 32.dp,
                    minTouchTarget = 56.dp
                )
            }
        }
    }
}

/**
 * CompositionLocal for accessing dimensions throughout the app
 */
val LocalNoveryDimensions = staticCompositionLocalOf {
    NoveryDimensions.fromDensity(UiDensity.DEFAULT)
}

/**
 * Convenience accessor for dimensions
 */
object NoveryTheme {
    val dimensions: NoveryDimensions
        @Composable
        get() = LocalNoveryDimensions.current
}

/**
 * Provider wrapper for dimensions
 */
@Composable
fun ProvideDimensions(
    density: UiDensity,
    content: @Composable () -> Unit
) {
    val dimensions = NoveryDimensions.fromDensity(density)
    CompositionLocalProvider(
        LocalNoveryDimensions provides dimensions,
        content = content
    )
}