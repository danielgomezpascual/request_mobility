package com.personal.requestmobility.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.personal.requestmobility.R

// Set of
//
// Material typography styles to start with



// Define la familia de fuentes agrupando sus diferentes pesos
val Fira = FontFamily(
    Font(R.font.fira, FontWeight.Normal),
    Font(R.font.fira, FontWeight.Bold)
    // Puedes añadir más aquí: Italic, Light, Medium, etc.
    // Font(R.font.rubik_italic, FontWeight.Normal, FontStyle.Italic)
)
val Sira = FontFamily(
    Font(R.font.sira, FontWeight.Normal),
    Font(R.font.sira_sbold, FontWeight.Bold)
    // Puedes añadir más aquí: Italic, Light, Medium, etc.
    // Font(R.font.rubik_italic, FontWeight.Normal, FontStyle.Italic)
)



val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = Fira,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),


    bodyLarge = TextStyle(
        fontFamily = Fira,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )

    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)