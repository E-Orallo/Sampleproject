package simple.project.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EcoNatureDarkColorScheme = darkColorScheme(
    primary = LeafGreen,
    onPrimary = Color.White,
    primaryContainer = BambooGreen,
    onPrimaryContainer = SandWhite,
    secondary = Terracotta,
    onSecondary = SandWhite,
    secondaryContainer = EarthBrown,
    onSecondaryContainer = SandWhite,
    background = ForestDark,
    onBackground = SandWhite,
    surface = MossSurface,
    onSurface = SandWhite,
    surfaceVariant = MossSurface,
    onSurfaceVariant = PebbleGray,
    outline = Color(0xFF2A332A),
    error = EcoError,
)

@Composable
fun SampleprojectTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = EcoNatureDarkColorScheme,
        typography = Typography,
        content = content,
    )
}
