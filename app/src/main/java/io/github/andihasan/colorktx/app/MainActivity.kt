package io.github.andihasan.colorktx.app

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.github.andihasan.colorktx.ColorKtx
import io.github.andihasan.colorktx.ThemeChooserDialogBuilder
import io.github.andihasan.colorktx.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var lastThemeOrdinal: Int = -1
    private var lastThemeMode: Int = -1
    private var lastTrueBlack: Boolean = false
    private var lastDynamic: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        // Penting: applyToActivity harus dipanggil SEBELUM super.onCreate 
        // untuk menerapkan tema sebelum activity dibuat
        ColorKtx.applyToActivity(this)
        
        super.onCreate(savedInstanceState)
        
        val colorKtx = ColorKtx.getInstance(this)
        // Simpan state setelah tema diterapkan
        saveCurrentState(colorKtx)
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

    private fun saveCurrentState(colorKtx: ColorKtx) {
        lastThemeOrdinal = colorKtx.staticTheme.ordinal
        lastThemeMode = colorKtx.themeMode
        lastTrueBlack = colorKtx.isTrueBlack
        lastDynamic = colorKtx.isDynamicTheme
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

    override fun onRestart() {
        super.onRestart()
        val colorKtx = ColorKtx.getInstance(this)
        // Cek jika ADA salah satu yang berubah di activity lain (ComposeActivity)
        if (lastThemeOrdinal != colorKtx.staticTheme.ordinal ||
            lastThemeMode != colorKtx.themeMode ||
            lastTrueBlack != colorKtx.isTrueBlack ||
            lastDynamic != colorKtx.isDynamicTheme
        ) {
            recreate()
        }
    }
}