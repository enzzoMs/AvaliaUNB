package ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale

@Composable
fun AnimatedSplashScreen(
    splashAnimationDurationMs: Int,
    splashContent: @Composable (scaleModifier: Modifier) -> Unit,
    onSplashEnd: () -> Unit,
    targetValue: Float,
    modifier: Modifier
) {
    val scale = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = targetValue,
            animationSpec = tween(
                durationMillis = splashAnimationDurationMs
            )
        )
        onSplashEnd()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        splashContent(Modifier.scale(scale.value))
    }
}