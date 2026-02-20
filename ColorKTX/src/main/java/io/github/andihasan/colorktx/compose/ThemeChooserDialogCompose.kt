package io.github.andihasan.colorktx.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.andihasan.colorktx.Themes

@Preview(showBackground = true, widthDp = 400, heightDp = 600) // Tentukan ukuran tetap
@Composable
fun ThemeChooserDialogPreview() {
    MaterialTheme { // Bungkus dengan Tema agar styling-nya benar
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // Kita panggil langsung isinya (Dialog)
            // Jika tetap tidak muncul, coba panggil konten di dalam AlertDialog secara langsung
            ThemeChooserDialogCompose(
                onDismiss = {},
                onDefaultTheme = {},
                onThemeSelected = {}
            )
        }
    }
}

@Composable
fun ThemeChooserDialogCompose(
    initialTheme: Themes? = null,
    onDismiss: () -> Unit,
    onDefaultTheme: () -> Unit,
    onThemeSelected: (Themes) -> Unit
) {
    val themes = Themes.entries
    // State untuk menyimpan pilihan sementara sebelum menekan tombol "Pilih"
    var selectedTheme by remember { mutableStateOf(initialTheme) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Pilih Tema") },
        text = {
            Box(modifier = Modifier.height(300.dp)) { // Beri batas tinggi agar scrollable
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(themes) { theme ->
                        val isSelected = selectedTheme == theme
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(colorResource(theme.primaryColor))
                                .border(
                                    width = if (isSelected) 3.dp else 0.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable {
                                    selectedTheme = theme
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White // Atur kontras sesuai kebutuhan
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    selectedTheme?.let { onThemeSelected(it) }
                },
                enabled = selectedTheme != null // Tombol aktif jika ada yang dipilih
            ) {
                Text("Pilih")
            }
        },
        dismissButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Tombol Neutral / Default di sisi kiri
                TextButton(onClick = {
                    onDefaultTheme()
                    onDismiss()
                }) {
                    Text("Default")
                }
                
                // Tombol Batal di sisi kanan (sebelum tombol Pilih)
                TextButton(onClick = onDismiss) {
                    Text("Batal")
                }
            }
        }
    )
}
