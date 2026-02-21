// C:/Users/Dell/AndroidStudioProjects/ColorKTX/app/src/main/java/io/github/andihasan/colorktx/app/ui/theme/Theme.kt

package io.github.andihasan.colorktx.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import io.github.andihasan.colorktx.ColorKtx
import io.github.andihasan.colorktx.ThemeMode

@Composable
fun ColorKTXTheme(
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    // Gunakan remember agar instance tidak dibuat ulang terus menerus saat recomposition
    val colorKtx = remember { ColorKtx.getInstance(context) }

    // Logika penentuan dark mode berdasarkan ThemeMode (LIGHT = 1, DARK = 2, AUTO = 0)
    val darkTheme = when (colorKtx.themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        else -> isSystemInDarkTheme() // Follow System
    }

    val colorScheme = when {
        colorKtx.isDynamicTheme && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> {
            // PERBAIKAN: Gunakan primaryColor (R.color) bukan themeId (R.style)
            val primaryColor = colorResource(id = colorKtx.staticTheme.primaryColor)
            if (darkTheme) {
                darkColorScheme(
                    primary = primaryColor,
                    onPrimary = Color.White,
                    secondary = PurpleGrey80,
                    tertiary = Pink80,
                    // Opsional: Implementasi True Black jika aktif
                    surface = if (colorKtx.isTrueBlack) Color.Black else Color(0xFF1C1B1F),
                    background = if (colorKtx.isTrueBlack) Color.Black else Color(0xFF1C1B1F)
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