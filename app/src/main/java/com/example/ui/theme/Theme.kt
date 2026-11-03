package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = CamGreenPrimaryDark,
    onPrimary = CamGreenOnPrimaryDark,
    primaryContainer = CamGreenContainerDark,
    onPrimaryContainer = CamGreenOnContainerDark,
    secondary = CamGoldSecondaryDark,
    onSecondary = CamGoldOnSecondaryDark,
    secondaryContainer = CamGoldContainerDark,
    onSecondaryContainer = CamGoldOnContainerDark,
    tertiary = CamRedTertiaryDark,
    onTertiary = CamRedOnTertiaryDark,
    tertiaryContainer = CamRedContainerDark,
    onTertiaryContainer = CamRedOnContainerDark,
    background = CamBackgroundDark,
    onBackground = CamOnBackgroundDark,
    surface = CamSurfaceDark,
    onSurface = CamOnSurfaceDark,
    surfaceVariant = CamSurfaceVariantDark,
    onSurfaceVariant = CamOnSurfaceVariantDark,
    outline = CamOutlineDark
)

private val LightColorScheme = lightColorScheme(
    primary = CamGreenPrimary,
    onPrimary = CamGreenOnPrimary,
    primaryContainer = CamGreenContainer,
    onPrimaryContainer = CamGreenOnContainer,
    secondary = CamGoldSecondary,
    onSecondary = CamGoldOnSecondary,
    secondaryContainer = CamGoldContainer,
    onSecondaryContainer = CamGoldOnContainer,
    tertiary = CamRedTertiary,
    onTertiary = CamRedOnTertiary,
    tertiaryContainer = CamRedContainer,
    onTertiaryContainer = CamRedOnContainer,
    background = CamBackgroundLight,
    onBackground = CamOnBackgroundLight,
    surface = CamSurfaceLight,
    onSurface = CamOnSurfaceLight,
    surfaceVariant = CamSurfaceVariantLight,
    onSurfaceVariant = CamOnSurfaceVariantLight,
    outline = CamOutlineLight
)

@Composable
fun DemarchesCamTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our handcrafted national civic theme by default
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
