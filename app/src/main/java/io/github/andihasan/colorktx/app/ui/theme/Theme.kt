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

@Composable
fun ColorKTXTheme(
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    // Gunakan remember agar instance tidak dibuat ulang terus menerus saat recomposition
    val colorKtx = remember { ColorKtx.getInstance(context) }

    // Logika penentuan dark mode berdasarkan ThemeMode (LIGHT = 1, DARK = 2, AUTO = 0)
    // Perhatian: ColorKtx.applyToActivity harus dipanggil SEBELUM setContent di Activity
    // untuk memastikan configuration night mode sudah diupdate dengan benar
    val darkTheme = when (colorKtx.themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        else -> isSystemInDarkTheme() // Follow System - membaca dari context yang sudah diupdate
    }

    val colorScheme = when {
        colorKtx.isDynamicTheme && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> {
            // Ambil warna primer dari resource menggunakan context
            val primaryColorRes = colorKtx.staticTheme.primaryColor
            val primaryColor = Color(context.getColor(primaryColorRes))
            
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
                    // Warna surface dan background untuk dark theme
                    surface = if (colorKtx.isTrueBlack) Color.Black else Color(0xFF1C1B1F),
                    onSurface = if (colorKtx.isTrueBlack) Color.White else Color(0xFFE3E2E6),
                    background = if (colorKtx.isTrueBlack) Color.Black else Color(0xFF1C1B1F),
                    onBackground = if (colorKtx.isTrueBlack) Color.White else Color(0xFFE3E2E6),
                    // Warna surface variant
                    surfaceVariant = Color(0xFF49454F),
                    onSurfaceVariant = Color(0xFFCAC4D0),
                    outline = Color(0xFF938F99)
                )
            } else {
                lightColorScheme(
                    primary = primaryColor,
                    onPrimary = Color.White,
                    secondary = secondaryColor,
                    onSecondary = Color.White,
                    tertiary = tertiaryColor,
                    onTertiary = Color.White,
                    // Warna surface dan background untuk light theme - PENTING!
                    surface = Color(0xFFFEF7FF), // Background terang
                    onSurface = Color(0xFF1D1B20), // Teks gelap untuk contrast
                    background = Color(0xFFFEF7FF), // Background terang
                    onBackground = Color(0xFF1D1B20), // Teks gelap
                    // Warna surface variant
                    surfaceVariant = Color(0xFFE7E0EC),
                    onSurfaceVariant = Color(0xFF49454F),
                    outline = Color(0xFF79747E)
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