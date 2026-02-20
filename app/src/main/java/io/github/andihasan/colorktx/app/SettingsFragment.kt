package io.github.andihasan.colorktx.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.andihasan.colorktx.ThemeChooserDialogBuilder
import io.github.andihasan.colorktx.ColorKtx
import io.github.andihasan.colorktx.ThemeMode
import io.github.andihasan.colorktx.hasS
import io.github.andihasan.colorktx.app.R
import io.github.andihasan.colorktx.app.databinding.FragmentSettingsBinding

class SettingsFragment : BottomSheetDialogFragment() {

    private lateinit var colorKtx: ColorKtx

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        colorKtx = ColorKtx.Companion.getInstance(requireContext())
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (hasS()) {
            binding.dynamicGroup.check(if (colorKtx.isDynamicTheme) R.id.dynamic_on else R.id.dynamic_off)
            binding.dynamicGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (isChecked) {
                    val newValue = (checkedId == R.id.dynamic_on)
                    if (colorKtx.isDynamicTheme != newValue) { // Tambahkan pengecekan ini
                        colorKtx.isDynamicTheme = newValue
                        requireActivity().recreate()
                    }
                }
            }
        } else {
            binding.dynamicColorLabel.isVisible = false
            binding.dynamicGroup.isVisible = false
        }
        binding.themeGroup.check(
            when (colorKtx.themeMode) {
                ThemeMode.AUTO -> R.id.auto_theme
                ThemeMode.LIGHT -> R.id.light_theme
                ThemeMode.DARK -> R.id.dark_theme
                else -> R.id.auto_theme
            }
        )

        binding.trueBlackGroup.check(if (colorKtx.isTrueBlack) R.id.true_black_on else R.id.true_black_off)
        binding.trueBlackGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                colorKtx.isTrueBlack = when (checkedId) {
                    R.id.true_black_on -> true
                    else -> false
                }
                requireActivity().recreate()
            }
        }

        binding.themeGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                colorKtx.themeMode = when (checkedId) {
                    R.id.auto_theme -> ThemeMode.AUTO
                    R.id.light_theme -> ThemeMode.LIGHT
                    R.id.dark_theme -> ThemeMode.DARK
                    else -> ThemeMode.AUTO
                }
            }
        }
        binding.changeTheme.setOnClickListener {
            ThemeChooserDialogBuilder(requireContext())
                .setTitle(R.string.choose_theme)
                .setPositiveButton("OK") { _, theme ->
                    colorKtx.staticTheme = theme
                    requireActivity().recreate()
                }
                .setNegativeButton("Cancel")
                .setNeutralButton("Default") { _, _ ->
                    colorKtx.resetTheme()
                    requireActivity().recreate()
                }
                .setIcon(R.drawable.ic_brush)
                .create()
                .show()
        }
    }
}