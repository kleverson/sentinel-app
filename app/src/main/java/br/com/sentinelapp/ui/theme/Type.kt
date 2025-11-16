package br.com.sentinelapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import br.com.sentinelapp.R

val InterFont = FontFamily(
    Font(R.font.inter, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_bold, FontWeight.Bold),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
    Font(R.font.inter_thin, FontWeight.Light),
    Font(R.font.inter_black, FontWeight.Black)
)

fun pxToSp(px: Float): TextUnit = px.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = pxToSp(24f),
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = InterFont,
        fontWeight = FontWeight.Medium,
        fontSize = pxToSp(24f),
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),

    bodySmall = TextStyle(
        fontFamily = InterFont,
        fontWeight = FontWeight.Normal,
        fontSize = pxToSp(14f),
        letterSpacing = 0.4.sp
    ),
    labelMedium = TextStyle(
        fontFamily = InterFont,
        fontWeight = FontWeight.Bold,
        fontSize = pxToSp(18f),
        letterSpacing = 0.25.sp
    ),
    labelSmall = TextStyle(
        fontFamily = InterFont,
        fontWeight = FontWeight.Normal,
        fontSize = pxToSp(16f),
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    )
)

val Typography.buttonText get() = TextStyle(
    fontFamily = InterFont,
    fontWeight = FontWeight.Bold,
    fontSize = pxToSp(14f),
    letterSpacing = 0.25.sp
)

val Typography.ListItemTitle get() = TextStyle(
    fontFamily = InterFont,
    fontWeight = FontWeight.Medium,
    fontSize = pxToSp(16f),
    letterSpacing = 0.25.sp
)

val Typography.ListItemSubTitle get() = TextStyle(
    fontFamily = InterFont,
    fontWeight = FontWeight.Normal,
    fontSize = pxToSp(14f),
    letterSpacing = 0.25.sp
)