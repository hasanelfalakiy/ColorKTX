// C:/Users/Dell/AndroidStudioProjects/ColorKTX/app/src/main/java/io/github/andihasan/colorktx/app/ui/theme/Theme.kt

package io.github.andihasan.colorktx.app.ui.theme

import android.annotation.SuppressLint
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import io.github.andihasan.colorktx.ColorKtx
import io.github.andihasan.colorktx.ThemeMode

/**
 * Helper untuk membuat warna sekunder dan tersier yang harmonis
 * berdasarkan warna primer menggunakan Material3 tonal palette
 */
private fun generateHarmonizedColors(primary: Color, isDark: Boolean): Pair<Color, Color> {
    val hsl = FloatArray(3)
    val primaryArgb = primary.toArgb()
    android.graphics.Color.colorToHSV(primaryArgb, hsl)
    val hue = hsl[0]
    val sat = hsl[1]
    // hsl[2] adalah value/brightness, tidak digunakan dalam perhitungan ini
    
    return if (isDark) {
        // Untuk dark theme: sekunder lebih terang, tersier adalah complement
        val secondaryHue = (hue + 15) % 360f // sedikit shift
        val secondaryColor = Color(android.graphics.Color.HSVToColor(floatArrayOf(secondaryHue, sat * 0.6f, 0.85f)))
        
        val tertiaryHue = (hue + 30) % 360f // lebih jauh di color wheel
        val tertiaryColor = Color(android.graphics.Color.HSVToColor(floatArrayOf(tertiaryHue, sat * 0.5f, 0.90f)))
        
        Pair(secondaryColor, tertiaryColor)
    } else {
        // Untuk light theme: sekunder dan tersier lebih gelap
        val secondaryHue = (hue + 15) % 360f
        val secondaryColor = Color(android.graphics.Color.HSVToColor(floatArrayOf(secondaryHue, sat * 0.7f, 0.45f)))
        
        val tertiaryHue = (hue + 30) % 360f
        val tertiaryColor = Color(android.graphics.Color.HSVToColor(floatArrayOf(tertiaryHue, sat * 0.6f, 0.50f)))
        
        Pair(secondaryColor, tertiaryColor)
    }
}

@SuppressLint("LocalContextConfigurationRead")
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

    // SOLUSI: Buat Context khusus yang mengikuti variabel darkTheme kita
    val themedContext = remember(context, darkTheme) {
        val configuration = android.content.res.Configuration(context.resources.configuration)
        configuration.uiMode = (configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK.inv()) or
                if (darkTheme) android.content.res.Configuration.UI_MODE_NIGHT_YES
                else android.content.res.Configuration.UI_MODE_NIGHT_NO
        context.createConfigurationContext(configuration)
    }

    val colorScheme = when {
        colorKtx.isDynamicTheme && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> {
            // Gunakan themedContext untuk mengambil warna primer agar tidak salah folder res
            val primaryColorRes = colorKtx.staticTheme.primaryColor
            val primaryColor = Color(themedContext.getColor(primaryColorRes))
            
            // Generate warna sekunder dan tersier yang harmonis
            val (secondaryColor, tertiaryColor) = generateHarmonizedColors(primaryColor, darkTheme)

            if (darkTheme) {
                darkColorScheme(
                    primary = primaryColor,
                    onPrimary = Color.White,
                    secondary = secondaryColor,
                    onSecondary = Color.White,
                    tertiary = tertiaryColor,
                    onTertiary = Color.White,
                    // Implementasi True Black jika aktif
                    surface = if (colorKtx.isTrueBlack) Color.Black else Color(0xFF1C1B1F),
                    background = if (colorKtx.isTrueBlack) Color.Black else Color(0xFF1C1B1F),
                    onSurface = if (colorKtx.isTrueBlack) Color.White else Color(0xFFE3E2E6),
                    onBackground = if (colorKtx.isTrueBlack) Color.White else Color(0xFFE3E2E6)
                )
            } else {
                lightColorScheme(
                    primary = primaryColor,
                    onPrimary = Color.White,
                    secondary = secondaryColor,
                    onSecondary = Color.White,
                    tertiary = tertiaryColor,
                    onTertiary = Color.White
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