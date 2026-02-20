package io.github.andihasan.colorktx.app.ui.theme

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
import androidx.compose.ui.res.colorResource
import io.github.andihasan.colorktx.ColorKtx

@Composable
fun ColorKTXTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorKtx = ColorKtx.getInstance(context)
    
    val colorScheme = when {
        colorKtx.isDynamicTheme && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> {
            // Fix: Use staticTheme.primaryColor instead of getTheme()
            // getTheme() returns a Style resource ID, which colorResource() cannot load.
            val primaryColor = colorResource(id = colorKtx.staticTheme.primaryColor)
            if (darkTheme) {
                darkColorScheme(
                    primary = primaryColor,
                    onPrimary = Color.White,
                    secondary = PurpleGrey80,
                    tertiary = Pink80
                )
            } else {
                lightColorScheme(
                    primary = primaryColor,
                    onPrimary = Color.White,
                    secondary = PurpleGrey40,
                    tertiary = Pink40
                )
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
