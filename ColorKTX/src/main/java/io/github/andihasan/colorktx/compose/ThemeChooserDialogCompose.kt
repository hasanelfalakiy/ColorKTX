package io.github.andihasan.colorktx.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.andihasan.colorktx.Themes

@Preview(showBackground = true)
@Composable
fun ThemeChooserContentPreview() {
    MaterialTheme {
        ThemeChooserDialogCompose(
            onDismiss = { },
            onDefaultTheme = { },
            onThemeSelected = { }
        )
    }
}

@Composable
fun ThemeChooserContent(
    selectedTheme: Themes?,
    onThemeClick: (Themes) -> Unit
) {
    val themes = Themes.entries
    Box(modifier = Modifier.height(300.dp)) {
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
                        // Jika colorResource error di preview, coba ganti sementara ke Color.Gray untuk test
                        .background(colorResource(theme.primaryColor))
                        .border(
                            width = if (isSelected) 3.dp else 0.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable { onThemeClick(theme) },
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
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
    var selectedTheme by remember { mutableStateOf(initialTheme) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Brush,
                    contentDescription = "Icon tema",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pilih Tema")
            }
        },
        text = {
            ThemeChooserContent(
                selectedTheme = selectedTheme,
                onThemeClick = { selectedTheme = it }
            )
        },
        confirmButton = {
            TextButton(
                onClick = { selectedTheme?.let { onThemeSelected(it) } },
                enabled = selectedTheme != null
            ) {
                Text("Pilih")
            }
        },
        dismissButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = {
                    onDefaultTheme()
                    onDismiss()
                }) {
                    Text("Default")
                }
                TextButton(onClick = onDismiss) {
                    Text("Batal")
                }
            }
        }
    )
}