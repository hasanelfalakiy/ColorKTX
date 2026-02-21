package io.github.andihasan.colorktx

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

class ColorKtx(context: Context) {
    private val prefs = context.getSharedPreferences("theme_engine_prefs", Context.MODE_PRIVATE)

    private var isFirstStart
        get() = prefs.getBoolean(FIRST_START, true)
        set(value) = prefs.edit(commit = true) { putBoolean(FIRST_START, value) }

    init {
        if (isFirstStart) {
            setDefaultValues()
            isFirstStart = false
        }
    }

    /**
     * Returns current [ThemeMode].
     * Setting this property applies the given theme mode to the activity.
     */
    var themeMode: Int
        get() = prefs.getInt(THEME_MODE, ThemeMode.AUTO)
        set(value) {
            require(value in 0..2) {
                "Incompatible value! Set this property with help of ThemeMode object."
            }
            prefs.edit(commit = true) { putInt(THEME_MODE, value) }
            AppCompatDelegate.setDefaultNightMode(
                when (value) {
                    ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                    ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
            )
        }

    private val nightMode
        get() = when (themeMode) {
            ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

    /**
     * Returns true if Dynamic Colors are enabled, false otherwise.
     * Setting this property to true enables dynamic colors, false disables dynamic colors.
     * Keep in mind that dynamic colors will work only on Android 12 i.e. API 31 and higher devices.
     * And call Activity.recreate() after changing this property so that the changes get applied to the activity.
     */
    var isDynamicTheme
        get() = prefs.getBoolean(DYNAMIC_THEME, Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        set(value) = prefs.edit(commit = true) { putBoolean(DYNAMIC_THEME, value) }

    /**
     * Get current app theme.
     * @return a dynamic theme if [isDynamicTheme] is enabled or a static theme otherwise.
     */
    fun getTheme(): Int {
        return if (hasS() && isDynamicTheme) R.style.Theme_ColorKtx_Dynamic else staticTheme.themeId
    }

    /**
     * Get current static app theme, the theme which is used when dynamic color is disabled
     */
    var staticTheme: Themes
        get() {
            val ordinal = prefs.getInt(APP_THEME, 14)
            return if (ordinal in Themes.entries.indices) Themes.entries[ordinal] else Themes.entries[14]
        }
        set(value) {
            prefs.edit(commit = true) { 
                putInt(APP_THEME, value.ordinal)
                // Disable dynamic theme when a specific color is picked
                putBoolean(DYNAMIC_THEME, false)
            }
        }

    /**
     * Resets static theme
     */
    fun resetTheme() {
        prefs.edit(commit = true) { 
            remove(APP_THEME) 
            putBoolean(DYNAMIC_THEME, hasS())
        }
    }

    var isTrueBlack
        get() = prefs.getBoolean(TRUE_BLACK, false)
        set(value) = prefs.edit(commit = true) { putBoolean(TRUE_BLACK, value) }

    private fun setDefaultValues() {
        // Hanya set default jika belum pernah di-set sebelumnya
        if (!prefs.contains(APP_THEME)) {
            prefs.edit(commit = true) { 
                putInt(APP_THEME, 14) // Default: Purple
            }
        }
        if (!prefs.contains(THEME_MODE)) {
            prefs.edit(commit = true) {
                putInt(THEME_MODE, ThemeMode.AUTO)
            }
        }
        if (!prefs.contains(TRUE_BLACK)) {
            prefs.edit(commit = true) {
                putBoolean(TRUE_BLACK, false)
            }
        }
        if (!prefs.contains(DYNAMIC_THEME)) {
            prefs.edit(commit = true) {
                putBoolean(DYNAMIC_THEME, hasS())
            }
        }
    }

    companion object {
        private var INSTANCE: ColorKtx? = null

        @JvmStatic
        fun getInstance(context: Context): ColorKtx {
            val currentInstance = INSTANCE

            if (currentInstance != null) {
                return currentInstance
            }

            synchronized(this) {
                val newInstance = ColorKtx(context.applicationContext)
                INSTANCE = newInstance
                return newInstance
            }
        }

        /**
         * Applies themes and night mode to all activities by registering a [ActivityLifecycleCallbacks] to your application.
         * @param application Target Application
         */
        @JvmStatic
        fun applyToActivities(application: Application) {
            application.registerActivityLifecycleCallbacks(ThemeEngineActivityCallback())
        }

        /**
         * Applies themes and night mode to given activity
         * @param activity Target activity
         */
        @JvmStatic
        fun applyToActivity(activity: Activity) {
            with(getInstance(activity)) {
                // Apply night mode terlebih dahulu
                AppCompatDelegate.setDefaultNightMode(nightMode)
                
                // Untuk ComponentActivity (non-AppCompat), kita perlu set night mode secara manual
                // pada configuration activity agar isSystemInDarkTheme() bekerja dengan benar
                val currentNightMode = when (themeMode) {
                    ThemeMode.LIGHT -> false
                    ThemeMode.DARK -> true
                    else -> null // Follow system, tidak perlu override
                }
                
                if (currentNightMode != null) {
                    val configuration = activity.resources.configuration
                    val newUiMode = if (currentNightMode) {
                        android.content.res.Configuration.UI_MODE_NIGHT_YES
                    } else {
                        android.content.res.Configuration.UI_MODE_NIGHT_NO
                    }
                    
                    if ((configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) != newUiMode) {
                        configuration.uiMode = (configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK.inv()) or newUiMode
                        @Suppress("DEPRECATION")
                        activity.resources.updateConfiguration(configuration, activity.resources.displayMetrics)
                    }
                }
                
                // Terapkan tema dari library ColorKTX
                activity.theme.applyStyle(getTheme(), true)
                if (isTrueBlack) {
                    activity.theme.applyStyle(R.style.ThemeOverlay_Black, true)
                }
            }
        }

        private const val THEME_MODE = "theme_mode"
        private const val DYNAMIC_THEME = "dynamic_theme"
        private const val APP_THEME = "app_theme"
        private const val TRUE_BLACK = "true_black"
        private const val FIRST_START = "first_start"
    }
}

private class ThemeEngineActivityCallback : ActivityLifecycleCallbacks {
    override fun onActivityPreCreated(
        activity: Activity,
        savedInstanceState: Bundle?
    ) {
        ColorKtx.applyToActivity(activity)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}
