package fr.isen.vittenet.isensmartcompanion.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColorScheme(
    primary = electric_blue,
    onPrimary = white,
    secondary = pastel_green,
    tertiary = pink_accent,
    onTertiary = white,

    background = black,
    surface = black ,

    secondaryContainer = pink_accent,
    onSurface = white,
    onSecondaryContainer = white,
    onSecondary = white,
    surfaceVariant = pastel_blue,
    onSurfaceVariant = white,

    inversePrimary = white

)

private val LightColorScheme = lightColorScheme(
    primary = pastel_blue,
    onPrimary = black,
    secondary = pink_accent,
    tertiary = electric_blue,
    onTertiary = white,
    background = white,

    secondaryContainer = pastel_blue,
    surface = white,
    onSurface = electric_blue,
    onSecondaryContainer = electric_blue,
    onSecondary = white,
    surfaceVariant = light_grey,
    onSurfaceVariant = black,
    inversePrimary = black
)

@Composable
fun ISENSmartCompanionTheme(
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

    val systemUiController = rememberSystemUiController()
    LaunchedEffect(colorScheme) {
        systemUiController.setStatusBarColor(
            color = colorScheme.primary,
            darkIcons = false
        )
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
