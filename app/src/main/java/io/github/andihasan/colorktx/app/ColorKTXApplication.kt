package io.github.andihasan.colorktx.app

import android.app.Application
import io.github.andihasan.colorktx.ColorKtx

class ColorKTXApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Menerapkan tema ke semua activity melalui lifecycle callbacks
        ColorKtx.applyToActivities(this)
    }
}
