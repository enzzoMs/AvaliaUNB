package utils.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NavigationComponent(
    navigationController: NavigationController,
    getDestination: (@Composable (destination: Screen) -> Unit),
    transitionAnimationDurationMs: Int = 500,
    modifier: Modifier = Modifier
) {
     Box(
         modifier = modifier
     ) {
        Crossfade(
            targetState = navigationController.currentDestination.value,
            animationSpec = tween(transitionAnimationDurationMs),
            modifier = Modifier
                .fillMaxSize()
        ) {
            getDestination(it)
        }
    }
}