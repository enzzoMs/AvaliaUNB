package ui.screens.main.viewmodel

import androidx.compose.ui.graphics.vector.ImageVector
import data.models.UserModel

data class MainScreenUiState(
    val userModel: UserModel,
    val pageTitle: String = "",
    val pageIcon: ImageVector? = null,
    val selectedNavItemIndex: Int? = null,
    val onEditProfile: Boolean = false
)