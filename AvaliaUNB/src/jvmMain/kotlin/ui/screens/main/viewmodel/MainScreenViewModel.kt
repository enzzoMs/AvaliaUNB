package ui.screens.main.viewmodel

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ui.components.NavigationItem
import javax.inject.Inject

class MainScreenViewModel @Inject constructor(){
    private val _mainScreenUiState = MutableStateFlow(MainScreenUiState())
    val mainScreenUiState = _mainScreenUiState.asStateFlow()

    fun updateNavItem(newNavItem: NavigationItem) {
        _mainScreenUiState.update { mainScreenUiState ->
            mainScreenUiState.copy(
                currentSelectedNavItem = newNavItem
            )
        }
    }
}