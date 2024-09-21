package com.softcross.eatzy.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.softcross.eatzy.common.EatzySingleton

private val DarkColorScheme = darkColorScheme(
)

private val LightColorScheme = lightColorScheme(
)

@Composable
fun EatzyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    TextColor = if (darkTheme) PrimaryWhite else PrimaryBlack
    BackgroundColor = if (darkTheme) PrimaryGray else PrimaryWhite
    PrimaryContainerColor = if (darkTheme) SecondaryGray else SubWhite
    PrimaryTextFieldColor = if (darkTheme) SecondaryWhite else PrimaryWhite
    PrimaryButtonColor = if (darkTheme) PrimaryOrange else PrimaryBlack
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
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