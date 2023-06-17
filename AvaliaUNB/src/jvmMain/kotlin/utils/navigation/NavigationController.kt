package utils.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class NavigationController(
    startDestination: String
) {
    var destinations: (@Composable (destinationLabel: String) -> Unit)? = null

    private var _currentDestination = mutableStateOf(startDestination)
    val currentDestination: State<String> = _currentDestination

    fun navigateTo(destination: String) {
        _currentDestination.value = destination
    }

    @Composable
    fun GetDestination(destinationLabel: String) {
        destinations?.let { it(destinationLabel) }
    }
}