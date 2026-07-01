package com.example.studywisely.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PurpleMain,
    secondary = PurpleText,
    background = BackgroundMain,
    surface = White,
    onPrimary = White,
    onSecondary = White,
    onBackground = PurpleText,
    onSurface = PurpleText
)

@Composable
fun StudyWiselyTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
