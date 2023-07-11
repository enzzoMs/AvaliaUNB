package ui.screens.main.viewmodel

import androidx.compose.ui.graphics.vector.ImageVector
import data.models.ClassModel
import data.models.SubjectModel
import data.models.TeacherModel
import data.models.UserModel
import ui.components.navigation.NavigationItem
import utils.navigation.Screen

data class MainScreenUiState(
    val userModel: UserModel,
    val pageTitle: String = "",
    val pageIcon: ImageVector? = null,
    val selectedNavItemIndex: Int? = null,
    val onEditProfile: Boolean = false,
    val currentScreen: Screen,
    val navItems: List<NavigationItem> = listOf(),
    val selectedSubject: SubjectModel? = null,
    val selectedClass: ClassModel? = null,
    val selectedTeacher: TeacherModel? = null
)