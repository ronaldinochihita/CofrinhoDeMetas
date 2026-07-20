package pt.ipbeja.cofrinho.ui.common

import androidx.compose.ui.graphics.Color

fun hexParaCor(hex: String, fallback: Color = Color(0xFF4CAF50)): Color = try {
    Color(android.graphics.Color.parseColor(hex))
} catch (e: IllegalArgumentException) {
    fallback
}
