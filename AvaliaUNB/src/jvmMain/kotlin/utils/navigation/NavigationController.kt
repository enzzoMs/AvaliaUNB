package utils.navigation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class NavigationController(
    startDestination: Screen
) {
    private var _currentDestination = mutableStateOf(startDestination)
    val currentDestination: State<Screen> = _currentDestination

    private val backStack: MutableList<Screen> = mutableListOf()

    fun navigateTo(destination: Screen) {
        backStack.add(_currentDestination.value)
        _currentDestination.value = destination
    }

    fun navigateBack() {
        if (backStack.isNotEmpty()) {
            _currentDestination.value = backStack.removeLast()
        }
    }
}