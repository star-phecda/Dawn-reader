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
    primary = DawnSitePink,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF4F0D34),
    onPrimaryContainer = Color(0xFFFFD8EB),
    secondary = DawnSiteGold,
    onSecondary = DawnSiteInk,
    secondaryContainer = Color(0xFF3A2A16),
    onSecondaryContainer = Color(0xFFFFDDB8),
    tertiary = DawnLavender,
    onTertiary = DawnSiteInk,
    tertiaryContainer = Color(0xFF332A49),
    onTertiaryContainer = Color(0xFFE8DEFF),
    background = DawnSiteNight,
    onBackground = DawnSiteCream,
    surface = DawnSiteSurface,
    onSurface = DawnSiteCream,
    surfaceVariant = Color(0xFF2A232D),
    onSurfaceVariant = Color(0xFFCEC4CE),
    surfaceContainerLowest = DawnSiteNight,
    surfaceContainerLow = Color(0xFF110E14),
    surfaceContainer = Color(0xFF1D1820),
    surfaceContainerHigh = Color(0xFF241E28),
    surfaceContainerHighest = Color(0xFF2D2631),
    inverseSurface = DawnSiteCream,
    inverseOnSurface = DawnSiteInk,
    inversePrimary = Color(0xFF9D005B),
    error = Error,
    onError = Color.White,
    errorContainer = Color(0xFF551D2D),
    onErrorContainer = Color(0xFFFFB8C5),
    outline = Color(0xFF514750),
    outlineVariant = Color(0xFF3A313B),
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
    primary = DawnSitePink,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFD7E8),
    onPrimaryContainer = Color(0xFF5A0735),
    secondary = DawnSiteGold,
    onSecondary = DawnSiteInk,
    secondaryContainer = Color(0xFFF0E1C9),
    onSecondaryContainer = Color(0xFF4A3213),
    tertiary = DawnViolet,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFE8E1FF),
    onTertiaryContainer = Color(0xFF2C235E),
    background = DawnSiteCream,
    onBackground = DawnSiteInk,
    surface = Color(0xFFFFFCF8),
    onSurface = DawnSiteInk,
    surfaceVariant = Color(0xFFE8DFD5),
    onSurfaceVariant = Color(0xFF5D555D),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFFBF6EF),
    surfaceContainer = Color(0xFFF1E8DE),
    surfaceContainerHigh = Color(0xFFE9DED3),
    surfaceContainerHighest = Color(0xFFE1D4C8),
    error = Error,
    onError = Color.White,
    errorContainer = Color(0xFFFFD9E1),
    onErrorContainer = Color(0xFF6A1A2D),
    outline = Color(0xFF968A83),
    outlineVariant = Color(0xFFD2C5BA)
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

    // For light mode, we use lighter backgrounds
    val background = Color(0xFFFAFAFA)
    val surface = Color.White

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
    extraSmall = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(18.dp),
    medium = RoundedCornerShape(24.dp),
    large = RoundedCornerShape(32.dp),
    extraLarge = RoundedCornerShape(40.dp)
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