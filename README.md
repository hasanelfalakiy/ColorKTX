# ColorKTX
[![](https://jitpack.io/v/hasanelfalakiy/ColorKTX.svg)](https://jitpack.io/#hasanelfalakiy/ColorKTX)

Library tema Material3 dengan fitur:
- 20+ tema warna
- Dark/Light mode
- Dynamic Color (Android 12+)
- Compose support
- True Black mode

## How to use
### Getting Started
Add the following gradle dependency to your build.gradle.kts:
```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

// build.gradle.kts (app)
dependencies {
    implementation("com.github.andihasan:ColorKTX:${version}")
}
```
### Usage
Theming is handled by ColorKtx class. It is a singleton class and you can get the ThemeEngine instance as follows:
```kotlin
val colorKtx = ColorKtx.getInstance(context)
```

Apply theme and night mode to given activity, in your activity's onCreate() method, call:
```kotlin
colorKtx.applyToActivity(this)
```

Apply theme and night mode to all activities by registering a ActivityLifecycleCallbacks to your application. In your application class’ onCreate() method, call:
```kotlin
colorKtx.applyToActivities(this)
```

Set Dark theme
>Note : Dark theme change is handled no need to call activity.recreate()
```kotlin
// Light mode
colorKtx.themeMode = ThemeMode.LIGHT

// Dark mode
colorKtx.themeMode = ThemeMode.DARK

// Follow System
colorKtx.themeMode = ThemeMode.AUTO
```

Set Dynamic Colors, you may want to recreate activity after settings this property
```kotlin
colorKtx.isDynamicTheme = true
```

Set static theme
> The theme used when dynamic color is disabled
```kotlin
colorKtx.staticTheme = R.style.Theme_ColorKtx_Orange
```

Create ThemeChooserDialog for the user to choose static theme from, this returns an AlertDialog.
```kotlin
ThemeChooserDialogBuilder(this)

    // Set Title
    .setTitle(R.string.choose_theme)
    
    // Set positive button text and an OnClickListener on the button
    .setPositiveButton("OK") { position, theme ->
      // This sets staticTheme
      colorKtx.staticTheme = theme
      recreate()
    }
    
    // Set negative button text
    .setNegativeButton("Cancel")
    
    // Set neutral button text and an OnClickListener on the button
    .setNeutralButton("Default") { _, _ ->
        // This resets static theme
        colorKtx.resetTheme()
        // Recreates activity
        recreate()
    }
    
    // Set Dialog Icon
    .setIcon(R.drawable.ic_brush)
    
    // Create Alert Dialog
    .create()
    // Show Dialog
    .show()
```

You can also use from Jetpack Compose like this:
```kotlin
ThemeChooserDialogCompose(
    initialTheme = colorKtx.staticTheme,
    onDismiss = { showDialog = false },
    onDefaultTheme = {
        colorKtx.resetTheme()
        recreate() 
    },
    onThemeSelected = { theme ->
        colorKtx.staticTheme = theme
        showDialog = false
        recreate()
    }
)
```

Get current app theme, this returns a dynamic theme when dynamic color is enabled, a static theme otherwise.
```kotlin
val theme = colorKtx.getTheme()
```

Reset static theme to default
```kotlin
colorKtx.resetTheme()
```

## Working
ThemeOverlays are used to apply theme colors to activities. This library contains 21 ThemeOverlays one of which is Dynamic which is used only when dynamic color is enabled other are used by user's choice.  
The themes are created with [MaterialThemeBuilder](https://material-foundation.github.io/material-theme-builder/).

## Lisence
```

Copyright [2022] [prathameshmm02] author/main developer
Copyright [2023] [dreamncn] contributor
Copyright [2026] [hasanelfalakiy] contributor

Licensed under the Apache License, Version 2.0 (the "License").

```


