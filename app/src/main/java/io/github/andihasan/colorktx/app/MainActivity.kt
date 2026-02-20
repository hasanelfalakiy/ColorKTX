package io.github.andihasan.colorktx.app

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.github.andihasan.colorktx.ColorKtx
import io.github.andihasan.colorktx.ThemeChooserDialogBuilder
import io.github.andihasan.colorktx.ThemeMode
import io.github.andihasan.colorktx.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val colorKtx = ColorKtx.getInstance(this)
        colorKtx.isDynamicTheme = false
        val currentTheme = colorKtx.themeMode

        // Ambil state dari SharedPreferences
        when (currentTheme) {
            1 -> AppCompatDelegate.MODE_NIGHT_NO
            2 -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

        // ColorKtx.Companion.applyToActivity(this)
        super.onCreate(savedInstanceState)
        ColorKtx.applyToActivity(this)
        // enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.changeTheme.setOnClickListener {
            ThemeChooserDialogBuilder(this)
                .setTitle(R.string.choose_theme)
                .setPositiveButton("OK") { _, theme ->
                    colorKtx.staticTheme = theme
                    recreate()
                }
                .setNegativeButton("Cancel")
                .setNeutralButton("Default") { _, _ ->
                    colorKtx.resetTheme()
                    recreate()
                }
                .setIcon(R.drawable.ic_brush)
                .create()
                .show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                SettingsFragment().show(supportFragmentManager, "Settings")
            }
            R.id.compose_preview -> {
                startActivity(Intent(this, ComposeActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /*override fun onResume() {
        super.onResume()
        val colorKtx = ColorKtx.getInstance(this)
        val theme = colorKtx.getTheme()
        colorKtx.themeMode = when (theme) {
            1 -> ThemeMode.LIGHT
            2 -> ThemeMode.DARK
            else -> ThemeMode.AUTO
        }
        recreate() // refresh UI
    }*/
}