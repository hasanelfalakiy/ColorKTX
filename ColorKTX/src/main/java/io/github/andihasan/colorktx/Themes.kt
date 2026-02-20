package io.github.andihasan.colorktx

import androidx.annotation.ColorRes
import androidx.annotation.StyleRes

enum class Theme(@StyleRes val themeId: Int, @ColorRes val primaryColor: Int) {
    Amber(R.style.Theme_ColorKtx_Amber, R.color.amber_primary),
    Blue(R.style.Theme_ColorKtx_Blue, R.color.blue_primary),
    BlueVariant(R.style.Theme_ColorKtx_BlueVariant, R.color.blue_variant_primary),
    Brown(R.style.Theme_ColorKtx_Brown, R.color.brown_primary),
    Cyan(R.style.Theme_ColorKtx_Cyan, R.color.cyan_primary),
    DeepOrange(R.style.Theme_ColorKtx_DeepOrange, R.color.deep_orange_primary),
    DeepPurple(R.style.Theme_ColorKtx_DeepPurple, R.color.deep_purple_primary),
    Green(R.style.Theme_ColorKtx_Green, R.color.green_primary),
    Indigo(R.style.Theme_ColorKtx_Indigo, R.color.indigo_primary),
    LightBlue(R.style.Theme_ColorKtx_LightBlue, R.color.light_blue_primary),
    LightGreen(R.style.Theme_ColorKtx_LightGreen, R.color.light_green_primary),
    Lime(R.style.Theme_ColorKtx_Lime, R.color.lime_primary),
    Orange(R.style.Theme_ColorKtx_Orange, R.color.orange_primary),
    Pink(R.style.Theme_ColorKtx_Pink, R.color.pink_primary),
    Purple(R.style.Theme_ColorKtx_Purple, R.color.purple_primary),
    Red(R.style.Theme_ColorKtx_Red, R.color.red_primary),
    Teal(R.style.Theme_ColorKtx_Teal, R.color.teal_primary),
    Violet(R.style.Theme_ColorKtx_Violet, R.color.violet_primary),
    Yellow(R.style.Theme_ColorKtx_Yellow, R.color.yellow_primary),
    SoftGreen(R.style.Theme_ColorKtx_SoftGreen, R.color.soft_green_primary)
}