package io.github.andihasan.colorktx.app

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.github.andihasan.colorktx.ThemeChooserDialogBuilder
import io.github.andihasan.colorktx.ThemeEngine
import io.github.andihasan.colorktx.app.R
import io.github.andihasan.colorktx.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeEngine.Companion.applyToActivity(this)
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val themeEngine = ThemeEngine.Companion.getInstance(this)

        binding.changeTheme.setOnClickListener {
            ThemeChooserDialogBuilder(this)
                .setTitle(R.string.choose_theme)
                .setPositiveButton("OK") { _, theme ->
                    themeEngine.staticTheme = theme
                    recreate()
                }
                .setNegativeButton("Cancel")
                .setNeutralButton("Default") { _, _ ->
                    themeEngine.resetTheme()
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
        if (item.itemId == R.id.settings) {
            SettingsFragment().show(supportFragmentManager, "Settings")
        }
        return super.onOptionsItemSelected(item)
    }
}