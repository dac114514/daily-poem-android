package com.claude.poem.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = ClaudePrimary,
    onPrimary = ClaudeOnPrimary,
    primaryContainer = ClaudePrimaryContainer,
    onPrimaryContainer = ClaudeOnPrimaryContainer,
    secondary = ClaudeSecondary,
    onSecondary = ClaudeOnSecondary,
    secondaryContainer = ClaudeSecondaryContainer,
    onSecondaryContainer = ClaudeOnSecondaryContainer,
    tertiary = ClaudeTertiary,
    onTertiary = ClaudeOnTertiary,
    tertiaryContainer = ClaudeTertiaryContainer,
    onTertiaryContainer = ClaudeOnTertiaryContainer,
    background = ClaudeBackground,
    onBackground = ClaudeOnBackground,
    surface = ClaudeSurface,
    onSurface = ClaudeOnSurface,
    surfaceVariant = ClaudeSurfaceVariant,
    onSurfaceVariant = ClaudeOnSurfaceVariant,
    outline = ClaudeOutline,
    outlineVariant = ClaudeOutlineVariant,
    error = ClaudeError,
    onError = ClaudeOnError,
)

private val DarkColorScheme = darkColorScheme(
    primary = ClaudePrimaryDark,
    onPrimary = ClaudeOnPrimaryDark,
    primaryContainer = ClaudePrimaryContainerDark,
    onPrimaryContainer = ClaudeOnPrimaryContainerDark,
    secondary = ClaudeSecondaryDark,
    onSecondary = ClaudeOnSecondaryDark,
    secondaryContainer = ClaudeSecondaryContainerDark,
    onSecondaryContainer = ClaudeOnSecondaryContainerDark,
    tertiary = ClaudeTertiaryDark,
    onTertiary = ClaudeOnTertiaryDark,
    tertiaryContainer = ClaudeTertiaryContainerDark,
    onTertiaryContainer = ClaudeOnTertiaryContainerDark,
    background = ClaudeBackgroundDark,
    onBackground = ClaudeOnBackgroundDark,
    surface = ClaudeSurfaceDark,
    onSurface = ClaudeOnSurfaceDark,
    surfaceVariant = ClaudeSurfaceVariantDark,
    onSurfaceVariant = ClaudeOnSurfaceVariantDark,
    outline = ClaudeOutlineDark,
    outlineVariant = ClaudeOutlineVariantDark,
    error = ClaudeErrorDark,
    onError = ClaudeOnErrorDark,
)

@Composable
fun AndroidStarterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content,
    )
}
