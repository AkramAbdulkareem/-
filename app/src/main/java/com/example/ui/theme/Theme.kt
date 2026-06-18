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
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = GoldAccent,
    secondary = GoldDim,
    tertiary = TealSuccess,
    background = ObsidianBG,
    surface = CardSlate,
    onPrimary = ObsidianBG,
    onSecondary = WhiteIce,
    onTertiary = ObsidianBG,
    onBackground = WhiteIce,
    onSurface = WhiteIce,
    error = ErrorRadical
  )

private val LightColorScheme =
  lightColorScheme(
    primary = GoldDim,
    secondary = GoldAccent,
    tertiary = TealSuccess,
    background = Color(0xFFF4F6F9),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color(0xFF1B1D25),
    onTertiary = Color.White,
    onBackground = Color(0xFF0F1013),
    onSurface = Color(0xFF0F1013),
    error = ErrorRadical
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // We default to darkTheme for that exquisite dark-gold premium brand appearance!
  dynamicColor: Boolean = false, // Set to false to preserve the luxury custom aesthetic
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
