package io.github.andihasan.colorktx.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.andihasan.colorktx.ColorKtx
import io.github.andihasan.colorktx.app.ui.theme.ColorKTXTheme
import io.github.andihasan.colorktx.compose.ThemeChooserDialogCompose

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        ColorKtx.applyToActivity(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val colorKtx = remember { ColorKtx.getInstance(this) }
            var showDialog by remember { mutableStateOf(false) }

            ColorKTXTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ComposeSettingScreen(
                        modifier = Modifier.padding(innerPadding),
                        onOpenDialog = {
                            showDialog = true
                        }
                    )

                    if (showDialog) {
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
                    }
                }
            }
        }
    }
}

@Composable
fun ComposeSettingScreen(
    modifier: Modifier = Modifier,
    onOpenDialog: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize()
            .padding(bottom = 120.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom

    ) {
        Text("Ini dari Compose Activity (ComponentActivity)")
        Spacer(modifier = Modifier.padding(top = 16.dp))
        Button(
            onClick = onOpenDialog
        ) {
            Text("Ganti warna")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ColorKTXTheme {
        ComposeSettingScreen()
    }
}
