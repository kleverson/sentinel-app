package br.com.sentinelapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.yourapp.ui.theme.DarkBackground
import com.example.yourapp.ui.theme.DarkBottomBar
import com.example.yourapp.ui.theme.DarkButtonBackground
import com.example.yourapp.ui.theme.DarkButtonText
import com.example.yourapp.ui.theme.DarkInputBackground
import com.example.yourapp.ui.theme.DarkNavActive
import com.example.yourapp.ui.theme.DarkSubtitle
import com.example.yourapp.ui.theme.DarkTitle
import com.example.yourapp.ui.theme.LightBackground
import com.example.yourapp.ui.theme.LightBottomBar
import com.example.yourapp.ui.theme.LightButtonBackground
import com.example.yourapp.ui.theme.LightButtonText
import com.example.yourapp.ui.theme.LightInputBackground
import com.example.yourapp.ui.theme.LightNavActive
import com.example.yourapp.ui.theme.LightSubtitle
import com.example.yourapp.ui.theme.LightTitle

private val DarkColors = darkColorScheme(
    background = DarkBackground,
    surface = DarkBackground,
    onBackground = DarkTitle,
    primary = DarkButtonBackground, // Botão
    onPrimary = DarkButtonText,
    secondary = DarkInputBackground,
    onSecondary = DarkSubtitle,

    // Esses não são obrigatórios mas ajudam
    tertiary = DarkBottomBar,
    onTertiary = DarkNavActive
)

private val LightColors = lightColorScheme(
    background = LightBackground,
    surface = LightBackground,
    onBackground = LightTitle,
    primary = LightButtonBackground,
    onPrimary = LightButtonText,
    secondary = LightInputBackground,
    onSecondary = LightSubtitle,

    tertiary = LightBottomBar,
    onTertiary = LightNavActive
)

@Composable
fun SentinelAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}