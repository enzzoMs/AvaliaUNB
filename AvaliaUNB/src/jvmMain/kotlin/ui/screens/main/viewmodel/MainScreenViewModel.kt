package ui.screens.main.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.School
import androidx.compose.ui.graphics.vector.ImageVector
import data.models.ClassModel
import data.models.SubjectModel
import data.models.TeacherModel
import data.models.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ui.components.navigation.NavigationItem
import ui.screens.main.NAV_ITEM_CLASSES_INDEX
import ui.screens.main.NAV_ITEM_SUBJECTS_INDEX
import ui.screens.main.NAV_ITEM_TEACHERS_INDEX
import utils.navigation.Screen
import utils.resources.ResourcesUtils

class MainScreenViewModel(
    userModel: UserModel
) {
    private val navItemSubjects = NavigationItem(
        ResourcesUtils.Strings.SUBJECTS, Icons.Outlined.School, NAV_ITEM_SUBJECTS_INDEX
    )
    private val navItemClasses = NavigationItem(
        ResourcesUtils.Strings.CLASSES, Icons.Outlined.CollectionsBookmark, NAV_ITEM_CLASSES_INDEX
    )
    private val navItemTeachers = NavigationItem(
        ResourcesUtils.Strings.TEACHERS, Icons.Outlined.Group, NAV_ITEM_TEACHERS_INDEX
    )

    private val _mainScreenUiState = MutableStateFlow(
        MainScreenUiState(
            userModel = userModel,
            currentScreen = Screen.SUBJECTS,
            navItems = listOf(navItemSubjects, navItemClasses, navItemTeachers)
        )
    )
    val mainScreenUiState = _mainScreenUiState.asStateFlow()

    private fun selectNavItem(navItem: NavigationItem) {
        _mainScreenUiState.update { mainScreenUiState ->
            mainScreenUiState.copy(
                onEditProfile = false,
                pageTitle = navItem.label,
                pageIcon = navItem.icon,
                selectedNavItemIndex = navItem.index
            )
        }
    }

    fun updateCurrentScreen(newScreen: Screen) {
        _mainScreenUiState.update { mainScreenUiState ->
            mainScreenUiState.copy(
                currentScreen = newScreen,
            )
        }

        when (newScreen) {
            Screen.SINGLE_CLASS, Screen.CLASSES -> selectNavItem(navItemClasses)
            Screen.SINGLE_SUBJECT, Screen.SUBJECTS -> selectNavItem(navItemSubjects)
            Screen.TEACHERS, Screen.SINGLE_TEACHER -> selectNavItem(navItemTeachers)
            else -> {}
        }
    }

    fun clearItemsSelection() {
        _mainScreenUiState.update { mainScreenUiState ->
            mainScreenUiState.copy(
                selectedSubject = null,
                selectedClass = null,
                selectedTeacher = null
            )
        }
    }

    fun setSubjectSelection(subjectModel: SubjectModel?) {
        _mainScreenUiState.update { mainScreenUiState ->
            mainScreenUiState.copy(
                selectedSubject = subjectModel
            )
        }
    }

    fun setClassSelection(classModel: ClassModel?) {
        _mainScreenUiState.update { mainScreenUiState ->
            mainScreenUiState.copy(
                selectedClass = classModel
            )
        }
    }

    fun setTeacherSelection(teacherModel: TeacherModel?) {
        _mainScreenUiState.update { mainScreenUiState ->
            mainScreenUiState.copy(
                selectedTeacher = teacherModel
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