package com.example.ui.theme

import androidx.compose.ui.graphics.Color

sealed class ThemeColors(
    val primary: Color,
    val secondary: Color,
    val background: Color,
    val tertiary: Color
) {
    object Night : ThemeColors(
        primary = Color(0xFF5780D9),
        secondary = Color(0xFF282C40),
        background = Color(0xFFFFFFFF),
        tertiary = Color(0xFF696C79),
    )

    object Day : ThemeColors(
        primary = Color(0xFF5780D9),
        secondary = Color(0xFF282C40),
        background = Color(0xFFFFFFFF),
        tertiary = Color(0xFF696C79),
    )
}