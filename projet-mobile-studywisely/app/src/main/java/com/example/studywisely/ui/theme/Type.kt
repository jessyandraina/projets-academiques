package com.example.studywisely.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.example.studywisely.R

val AndikaFont = FontFamily(
    Font(R.font.andika_regular)
)

val Typography = Typography(

    headlineSmall = TextStyle(
        fontFamily = AndikaFont,
        fontSize = 22.sp
    ),

    titleMedium = TextStyle(
        fontFamily = AndikaFont,
        fontSize = 18.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = AndikaFont,
        fontSize = 16.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = AndikaFont,
        fontSize = 14.sp
    )
)
