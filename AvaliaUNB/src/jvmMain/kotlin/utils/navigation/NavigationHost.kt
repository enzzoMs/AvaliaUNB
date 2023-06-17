package utils.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

class NavigationHost(
    private val navigationController: NavigationController,
    private val transitionAnimationDurationMs: Int = 1000,
    private val modifier: Modifier = Modifier
) {

    @Composable
    fun DisplayContent() {
        Box(
            modifier = modifier
        ) {
            Crossfade(
                targetState = navigationController.currentDestination.value,
                animationSpec = tween(transitionAnimationDurationMs),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                navigationController.GetDestination(it)
            }
        }
    }
}