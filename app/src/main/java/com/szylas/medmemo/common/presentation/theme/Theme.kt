 package com.szylas.medmemo.ui.ui.theme

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
    background = Gray,
    primary = LightBlue,
    secondary = DarkBlue,
    tertiary = AppBarBlack,
    onPrimary = Black,
    onSecondary = White,
    onTertiary = White,
    outline = DimBlue,
    surfaceDim = DimBlue,
    onSurface = AppBarWhite,
    surface = AppBarBlack,
    surfaceVariant = LightGray,
    surfaceTint = Green,
    error = Red,
    secondaryContainer = LightGray

)

private val LightColorScheme = lightColorScheme(
    background = White,
    primary = DarkerBlue,
    secondary = DarkBlue,
    tertiary = AppBarBlack,
    onPrimary = White,
    onSecondary = White,
    onTertiary = Black,
    outline = DimBlue,
    surfaceDim = DarkerBlue,
    onSurface = AppBarBlack,
    surface = AppBarWhite,
    surfaceVariant = LighterGray,
    surfaceTint = Green,
    error = Red,
    secondaryContainer = LightGray



    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun MedMemoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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