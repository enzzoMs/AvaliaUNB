package utils.navigation

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

private const val MAX_COLOR_COMPONENT_VALUE = 256
private const val MIN_COLOR_COMPONENT_VALUE = 0

object Utils {
    fun getRandomColorWithTransparency(transparency: Int): Color {
        val transparencyValue = when {
            transparency < MIN_COLOR_COMPONENT_VALUE -> MIN_COLOR_COMPONENT_VALUE
            transparency > MAX_COLOR_COMPONENT_VALUE -> MAX_COLOR_COMPONENT_VALUE
            else -> transparency
        }

        val red = Random.nextInt(MAX_COLOR_COMPONENT_VALUE)
        val green = Random.nextInt(MAX_COLOR_COMPONENT_VALUE)
        val blue = Random.nextInt(MAX_COLOR_COMPONENT_VALUE)
        return Color(red, green, blue, transparencyValue)
    }
}