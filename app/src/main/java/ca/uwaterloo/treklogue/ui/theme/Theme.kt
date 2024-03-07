package ca.uwaterloo.treklogue.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorPalette = lightColorScheme(
    primary = Blue200,
    inversePrimary = Purple700,
    secondary = Teal200
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
