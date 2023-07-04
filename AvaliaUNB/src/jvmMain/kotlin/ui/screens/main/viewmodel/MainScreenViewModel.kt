package ui.screens.main.viewmodel

import androidx.compose.ui.graphics.vector.ImageVector
import data.models.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ui.components.NavigationItem
import ui.screens.main.NAV_ITEM_CLASSES_INDEX
import ui.screens.main.NAV_ITEM_SUBJECTS_INDEX
import ui.screens.main.NAV_ITEM_TEACHERS_INDEX
import utils.navigation.Screen

class MainScreenViewModel(
    userModel: UserModel
) {
    private val _mainScreenUiState = MutableStateFlow(
        MainScreenUiState(userModel = userModel, currentScreen = Screen.SUBJECTS)
    )
    val mainScreenUiState = _mainScreenUiState.asStateFlow()

    fun onNavItemClicked(navItem: NavigationItem) {
        _mainScreenUiState.update { mainScreenUiState ->
            mainScreenUiState.copy(
                onEditProfile = false,
                pageTitle = navItem.label,
                pageIcon = navItem.icon,
                selectedNavItemIndex = navItem.index,
                currentScreen = when(navItem.index) {
                    NAV_ITEM_SUBJECTS_INDEX -> Screen.SUBJECTS
                    NAV_ITEM_CLASSES_INDEX -> Screen.CLASSES
                    NAV_ITEM_TEACHERS_INDEX -> Screen.TEACHERS
                    else -> Screen.SUBJECTS
                }
            )
        }
    }

    fun updateUserModel(newUserModel: UserModel) {
        _mainScreenUiState.update { mainScreenUiState ->
            mainScreenUiState.copy(
                userModel = newUserModel
            )
        }
    }

    fun updateCurrentScreen(newScreen: Screen) {
        _mainScreenUiState.update { mainScreenUiState ->
            mainScreenUiState.copy(
                currentScreen = newScreen
            )
        }
    }

    fun updatePageInformation(newTitle: String, newIcon: ImageVector?) {
        _mainScreenUiState.update { mainScreenUiState ->
            mainScreenUiState.copy(
                pageTitle = newTitle,
                pageIcon = newIcon
            )
        }
    }

    fun setIsEditingProfile(isEditing: Boolean) {
        _mainScreenUiState.update { mainScreenUiState ->
            mainScreenUiState.copy(
                onEditProfile = isEditing
            )
        }
    }
}