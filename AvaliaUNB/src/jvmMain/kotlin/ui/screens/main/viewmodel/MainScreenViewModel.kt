package ui.screens.main.viewmodel

import androidx.compose.ui.graphics.vector.ImageVector
import data.models.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainScreenViewModel(
    userModel: UserModel
) {
    private val _mainScreenUiState = MutableStateFlow(MainScreenUiState(userModel))
    val mainScreenUiState = _mainScreenUiState.asStateFlow()

    fun updateUserModel(newUserModel: UserModel) {
        _mainScreenUiState.update { mainScreenUiState ->
            mainScreenUiState.copy(
                userModel = newUserModel
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

    fun updateSelectedNavIndex(newIndex: Int) {
        _mainScreenUiState.update { mainScreenUiState ->
            mainScreenUiState.copy(
                selectedNavItemIndex = newIndex
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