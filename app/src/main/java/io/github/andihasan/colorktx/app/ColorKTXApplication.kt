package io.github.andihasan.colorktx.app

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import io.github.andihasan.colorktx.ColorKtx
import io.github.andihasan.colorktx.ThemeMode

class ColorKTXApplication : Application() {
    override fun onCreate() {
        // Set night mode SEBELUM super.onCreate() agar resources di-load dengan benar
        val colorKtx = ColorKtx.getInstance(this)
        val nightMode = when (colorKtx.themeMode) {
            ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)

        // Update configuration untuk memastikan resources di-load dengan night mode yang benar
        val currentNightMode = when (colorKtx.themeMode) {
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
            else -> null // Follow system
        }

        if (currentNightMode != null) {
            val config = resources.configuration
            val newUiMode = if (currentNightMode) {
                Configuration.UI_MODE_NIGHT_YES
            } else {
                Configuration.UI_MODE_NIGHT_NO
            }
            if ((config.uiMode and Configuration.UI_MODE_NIGHT_MASK) != newUiMode) {
                config.uiMode = (config.uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()) or newUiMode
                @Suppress("DEPRECATION")
                resources.updateConfiguration(config, resources.displayMetrics)
            }
        }

        super.onCreate()

        // Daftarkan lifecycle callbacks untuk activity yang akan dibuat
        ColorKtx.applyToActivities(this)
    }
}
