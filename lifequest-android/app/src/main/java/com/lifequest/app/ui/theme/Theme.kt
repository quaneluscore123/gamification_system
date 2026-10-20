package com.lifequest.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Cùng bảng màu với bản web (shared/ui/index.css)
object LqColors {
    val Background = Color(0xFF0F172A)
    val Card = Color(0xFF1E293B)
    val CardRaised = Color(0xFF334155)
    val Primary = Color(0xFF3B82F6)
    val TextMain = Color(0xFFF8FAFC)
    val TextMuted = Color(0xFF94A3B8)
    val Gold = Color(0xFFF59E0B)
    val Danger = Color(0xFFEF4444)
    val Success = Color(0xFF22C55E)
}

private val colorScheme = darkColorScheme(
    primary = LqColors.Primary,
    onPrimary = Color.White,
    secondary = LqColors.Gold,
    background = LqColors.Background,
    onBackground = LqColors.TextMain,
    surface = LqColors.Card,
    onSurface = LqColors.TextMain,
    surfaceVariant = LqColors.CardRaised,
    onSurfaceVariant = LqColors.TextMuted,
    surfaceContainer = LqColors.Card,
    error = LqColors.Danger,
)

@Composable
fun LifeQuestTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = colorScheme, content = content)
}
