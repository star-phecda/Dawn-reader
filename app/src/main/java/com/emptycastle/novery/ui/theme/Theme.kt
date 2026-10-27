package com.emptycastle.novery.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.emptycastle.novery.domain.model.AppSettings
import com.emptycastle.novery.domain.model.CustomThemeColors
import com.emptycastle.novery.domain.model.ThemeMode

/**
 * Dark color scheme - Primary theme for Novery
 */
private val DarkColorScheme = darkColorScheme(
    primary = DawnCyan,
    onPrimary = DawnNavy,
    primaryContainer = DawnNavy3,
    onPrimaryContainer = DawnCyan,
    secondary = DawnMagenta,
    onSecondary = DawnNavy,
    secondaryContainer = Color(0xFF4B3034),
    onSecondaryContainer = DawnRose,
    tertiary = DawnViolet,
    onTertiary = DawnNavy,
    tertiaryContainer = Color(0xFF3C354A),
    onTertiaryContainer = DawnLavender,
    background = DawnNavy,
    onBackground = Ink50,
    surface = DawnPanel,
    onSurface = Ink50,
    surfaceVariant = DawnNavy3,
    onSurfaceVariant = Ink300,
    surfaceContainerLowest = DawnNavy,
    surfaceContainerLow = DawnNavy2,
    surfaceContainer = DawnNavy3,
    surfaceContainerHigh = DawnPanel,
    surfaceContainerHighest = DawnPanelHi,
    inverseSurface = DawnSiteCream,
    inverseOnSurface = DawnSiteInk,
    inversePrimary = DawnMagenta,
    error = Error,
    onError = Color.White,
    errorContainer = Color(0xFF551D2D),
    onErrorContainer = Color(0xFFFFB8C5),
    outline = Color(0xFF615B4E),
    outlineVariant = Color(0xFF464236),
    scrim = Color.Black
)

/**
 * AMOLED Black color scheme
 */
private val AmoledDarkColorScheme = DarkColorScheme.copy(
    background = Color.Black,
    surface = Color.Black,
    surfaceContainerLowest = Color.Black,
    surfaceContainerLow = Color(0xFF0A0A0A),
    surfaceContainer = Color(0xFF121212),
    surfaceContainerHigh = Color(0xFF1A1A1A),
    surfaceContainerHighest = Color(0xFF222222)
)

/**
 * Light color scheme
 */
private val LightColorScheme = lightColorScheme(
    // Warm paper background with a butter-gold primary and restrained pastel accents.
    primary = Color(0xFF8F6A12),
    onPrimary = Color.White,
    primaryContainer = DawnButter,
    onPrimaryContainer = Color(0xFF30240B),
    secondary = Color(0xFF975760),
    onSecondary = Color.White,
    secondaryContainer = DawnRose,
    onSecondaryContainer = Color(0xFF3B1C22),
    tertiary = Color(0xFF64578D),
    onTertiary = Color.White,
    tertiaryContainer = DawnLilac,
    onTertiaryContainer = Color(0xFF2B1E42),
    background = DawnPaper,
    onBackground = DawnWarmInk,
    surface = DawnPaperRaised,
    onSurface = DawnWarmInk,
    surfaceVariant = Color(0xFFEDE8DC),
    onSurfaceVariant = Color(0xFF5D5648),
    surfaceContainerLowest = Color(0xFFFFFDF8),
    surfaceContainerLow = Color(0xFFF7F4EA),
    surfaceContainer = DawnPaperMuted,
    surfaceContainerHigh = Color(0xFFE8E3D7),
    surfaceContainerHighest = Color(0xFFDFDACD),
    error = Error,
    onError = Color.White,
    errorContainer = Color(0xFFF8D9D7),
    onErrorContainer = Color(0xFF601410),
    outline = Color(0xFF827969),
    outlineVariant = Color(0xFFD4CDBE)
)

/**
 * Creates a custom dark color scheme based on user-selected colors
 */
private fun createCustomDarkColorScheme(colors: CustomThemeColors): ColorScheme {
    val primary = Color(colors.primaryColor)
    val secondary = Color(colors.secondaryColor)
    val background = Color(colors.backgroundColor)
    val surface = Color(colors.surfaceColor)

    // Generate derived colors
    val onPrimary = getContrastColor(primary)
    val onSecondary = getContrastColor(secondary)
    val onBackground = getContrastColor(background)
    val onSurface = getContrastColor(surface)

    // Create lighter/darker variants
    val primaryContainer = primary.copy(alpha = 0.3f).compositeOver(background)
    val secondaryContainer = secondary.copy(alpha = 0.2f).compositeOver(surface)
    val surfaceVariant = blendColors(surface, Color.White, 0.05f)
    val surfaceContainer = blendColors(surface, Color.White, 0.08f)
    val surfaceContainerHigh = blendColors(surface, Color.White, 0.12f)
    val surfaceContainerHighest = blendColors(surface, Color.White, 0.16f)
    val surfaceContainerLow = blendColors(surface, background, 0.5f)

    return darkColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = primary,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = secondary,
        tertiary = secondary.copy(alpha = 0.8f),
        onTertiary = onSecondary,
        tertiaryContainer = secondaryContainer,
        onTertiaryContainer = secondary,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurface.copy(alpha = 0.7f),
        surfaceContainerLowest = background,
        surfaceContainerLow = surfaceContainerLow,
        surfaceContainer = surfaceContainer,
        surfaceContainerHigh = surfaceContainerHigh,
        surfaceContainerHighest = surfaceContainerHighest,
        inverseSurface = onSurface,
        inverseOnSurface = surface,
        inversePrimary = primary.copy(alpha = 0.8f),
        error = Error,
        onError = Color.White,
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),
        outline = surfaceContainerHigh,
        outlineVariant = surfaceContainer,
        scrim = Color.Black
    )
}

/**
 * Creates a custom light color scheme based on user-selected colors
 */
private fun createCustomLightColorScheme(colors: CustomThemeColors): ColorScheme {
    val primary = Color(colors.primaryColor)
    val secondary = Color(colors.secondaryColor)

    // Keep custom light themes on the same warm paper foundation.
    val background = DawnPaper
    val surface = DawnPaperRaised

    val onPrimary = getContrastColor(primary)
    val onSecondary = getContrastColor(secondary)

    val primaryContainer = primary.copy(alpha = 0.12f).compositeOver(surface)
    val secondaryContainer = secondary.copy(alpha = 0.12f).compositeOver(surface)

    return lightColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = primary.darken(0.3f),
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = secondary.darken(0.3f),
        background = background,
        onBackground = Zinc900,
        surface = surface,
        onSurface = Zinc900,
        surfaceVariant = Zinc100,
        onSurfaceVariant = Zinc700,
        error = Error,
        onError = Color.White,
        outline = Zinc300,
        outlineVariant = Zinc200
    )
}

/**
 * Utility function to get contrasting color (black or white)
 */
private fun getContrastColor(color: Color): Color {
    val luminance = 0.299 * color.red + 0.587 * color.green + 0.114 * color.blue
    return if (luminance > 0.5) Color.Black else Color.White
}

/**
 * Blend two colors together
 */
private fun blendColors(color1: Color, color2: Color, ratio: Float): Color {
    val inverseRatio = 1f - ratio
    return Color(
        red = (color1.red * inverseRatio + color2.red * ratio).coerceIn(0f, 1f),
        green = (color1.green * inverseRatio + color2.green * ratio).coerceIn(0f, 1f),
        blue = (color1.blue * inverseRatio + color2.blue * ratio).coerceIn(0f, 1f),
        alpha = 1f
    )
}

/**
 * Darken a color by a given factor
 */
private fun Color.darken(factor: Float): Color {
    return Color(
        red = (red * (1 - factor)).coerceIn(0f, 1f),
        green = (green * (1 - factor)).coerceIn(0f, 1f),
        blue = (blue * (1 - factor)).coerceIn(0f, 1f),
        alpha = alpha
    )
}

/**
 * Composite one color over another
 */
private fun Color.compositeOver(background: Color): Color {
    val fgAlpha = this.alpha
    val bgAlpha = background.alpha
    val outAlpha = fgAlpha + bgAlpha * (1f - fgAlpha)

    return if (outAlpha == 0f) {
        Color.Transparent
    } else {
        Color(
            red = (red * fgAlpha + background.red * bgAlpha * (1f - fgAlpha)) / outAlpha,
            green = (green * fgAlpha + background.green * bgAlpha * (1f - fgAlpha)) / outAlpha,
            blue = (blue * fgAlpha + background.blue * bgAlpha * (1f - fgAlpha)) / outAlpha,
            alpha = outAlpha
        )
    }
}

private val DawnShapes = Shapes(
    extraSmall = RoundedCornerShape(10.dp),
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(22.dp),
    large = RoundedCornerShape(30.dp),
    extraLarge = RoundedCornerShape(34.dp)
)

/**
 * Main theme composable for Dawn with Material 3 Expressive motion and shapes.
 */
@Composable
fun NoveryTheme(
    appSettings: AppSettings = AppSettings(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current

    // Determine if dark theme based on settings
    val useDarkTheme = when (appSettings.themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    // Select color scheme
    val colorScheme = when {
        // Custom theme takes priority (but not over dynamic color if that's enabled)
        appSettings.useCustomTheme && !appSettings.useDynamicColor -> {
            if (useDarkTheme) {
                val customScheme = createCustomDarkColorScheme(appSettings.customThemeColors)
                if (appSettings.amoledBlack) {
                    customScheme.copy(
                        background = Color.Black,
                        surface = Color.Black,
                        surfaceContainerLowest = Color.Black,
                        surfaceContainerLow = Color(0xFF0A0A0A),
                        surfaceContainer = Color(0xFF121212),
                        surfaceContainerHigh = Color(0xFF1A1A1A),
                        surfaceContainerHighest = Color(0xFF222222)
                    )
                } else {
                    customScheme
                }
            } else {
                createCustomLightColorScheme(appSettings.customThemeColors)
            }
        }
        // Dynamic color (Material You)
        appSettings.useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (useDarkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }
        // AMOLED black (only in dark mode)
        useDarkTheme && appSettings.amoledBlack -> AmoledDarkColorScheme
        // Standard dark
        useDarkTheme -> DarkColorScheme
        // Light
        else -> LightColorScheme
    }

    // Update system bars
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val backgroundColor = when {
                appSettings.useCustomTheme && !appSettings.useDynamicColor -> {
                    if (useDarkTheme) {
                        if (appSettings.amoledBlack) Color.Black
                        else Color(appSettings.customThemeColors.backgroundColor)
                    } else {
                        Zinc50
                    }
                }
                useDarkTheme -> {
                    if (appSettings.amoledBlack) Color.Black else Zinc950
                }
                else -> Zinc50
            }

            window.statusBarColor = backgroundColor.toArgb()
            window.navigationBarColor = backgroundColor.toArgb()

            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !useDarkTheme
                isAppearanceLightNavigationBars = !useDarkTheme
            }
        }
    }

    // Provide dimensions based on density
    ProvideDimensions(density = appSettings.uiDensity) {
        MaterialExpressiveTheme(
            colorScheme = colorScheme,
            motionScheme = MotionScheme.expressive(),
            shapes = DawnShapes,
            typography = NoveryTypography,
            content = content
        )
    }
}

/**
 * Legacy theme for backward compatibility (always dark)
 */
@Composable
fun NoveryTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    NoveryTheme(
        appSettings = AppSettings(
            themeMode = if (darkTheme) ThemeMode.DARK else ThemeMode.LIGHT
        ),
        content = content
    )
}