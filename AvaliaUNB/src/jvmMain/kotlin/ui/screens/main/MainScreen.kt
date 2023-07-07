package ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.models.SubjectModel
import di.DaggerComponentHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.resources.ResourcesUtils
import theme.*
import ui.components.NavigationItem
import ui.components.NavigationPanelColors
import ui.components.SideNavigationPanel
import ui.screens.classes.ClassesScreen
import ui.screens.main.viewmodel.MainScreenViewModel
import ui.screens.profile.ProfileScreen
import ui.screens.profile.viewmodel.ProfileViewModel
import ui.screens.subjects.single.SingleSubjectScreen
import ui.screens.subjects.all.SubjectsScreen
import ui.screens.subjects.single.viewmodel.SingleSubjectViewModel
import ui.screens.teachers.TeachersScreen
import utils.navigation.Screen

const val NAV_NO_SELECTED_ITEM_INDEX = -1
const val NAV_ITEM_SUBJECTS_INDEX = 0
const val NAV_ITEM_CLASSES_INDEX = 1
const val NAV_ITEM_TEACHERS_INDEX = 2

@Composable
fun MainScreen(
    mainScreenViewModel: MainScreenViewModel,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    val mainScreenUiState by mainScreenViewModel.mainScreenUiState.collectAsState()
    var selectedSubject by remember { mutableStateOf<SubjectModel?>(null) }

    Row {
        SideNavigationPanel(
            onLogoutClicked = onLogout,
            selectedNavIndex = when {
                mainScreenUiState.onEditProfile -> NAV_NO_SELECTED_ITEM_INDEX
                mainScreenUiState.selectedNavItemIndex == null -> {
                    mainScreenViewModel.updatePageInformation(
                        newTitle = ResourcesUtils.Strings.SUBJECTS,
                        newIcon = Icons.Outlined.School
                    )
                    NAV_ITEM_SUBJECTS_INDEX
                }
                else -> mainScreenUiState.selectedNavItemIndex!!
            },
            onItemClicked = { navItem -> mainScreenViewModel.onNavItemClicked(navItem) },
            modifier = Modifier
                .weight(1f)
        )
        Column(
            modifier = Modifier
                .weight(4f)
        ) {
            MainScreenContent(
                pageTitle = if (mainScreenUiState.onEditProfile) {
                    ResourcesUtils.Strings.PROFILE
                } else {
                    mainScreenUiState.pageTitle
                },
                pageIcon = if (mainScreenUiState.onEditProfile) {
                    Icons.Outlined.Badge
                } else {
                    mainScreenUiState.pageIcon
                },
                userName = mainScreenUiState.userModel.name,
                userProfilePicture = mainScreenUiState.userModel.profilePicture,
                onEditProfileClicked = {
                    mainScreenViewModel.updateCurrentScreen(Screen.PROFILE)
                    mainScreenViewModel.setIsEditingProfile(true)
                },
                currentScreen = mainScreenUiState.currentScreen,
                getScreenContent = { screen ->
                    when(screen) {
                        Screen.SUBJECTS -> SubjectsScreen(
                            subjectsViewModel = DaggerComponentHolder.appComponent.getSubjectsViewModel(),
                            onSubjectClicked = { subjectModel ->
                                selectedSubject = subjectModel
                                mainScreenViewModel.updateCurrentScreen(Screen.SINGLE_SUBJECT)
                            }
                        )
                        Screen.SINGLE_SUBJECT -> SingleSubjectScreen(
                            singleSubjectViewModel = SingleSubjectViewModel(
                               subjectModel = selectedSubject!!,
                               subjectRepository = DaggerComponentHolder.appComponent.getSubjectRepository()
                            ),
                            onBackClicked = {
                                mainScreenViewModel.updateCurrentScreen(Screen.SUBJECTS)
                                selectedSubject = null
                            }
                        )
                        Screen.CLASSES -> ClassesScreen()
                        Screen.TEACHERS -> TeachersScreen()
                        Screen.PROFILE -> {
                            val profileViewModel = ProfileViewModel(
                                mainScreenUiState.userModel,
                                DaggerComponentHolder.appComponent.getUserRepository()
                            )

                            ProfileScreen(
                                profileViewModel = profileViewModel,
                                onBackClicked = {
                                    mainScreenViewModel.setIsEditingProfile(false)
                                    mainScreenViewModel.updateCurrentScreen(
                                        when (mainScreenUiState.selectedNavItemIndex) {
                                            null -> Screen.SUBJECTS
                                            NAV_ITEM_SUBJECTS_INDEX -> Screen.SUBJECTS
                                            NAV_ITEM_CLASSES_INDEX -> Screen.CLASSES
                                            else -> Screen.TEACHERS
                                        }
                                    )
                                },
                                onFinishEditClicked = {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        val updateUserResult = withContext(Dispatchers.Default) {
                                            profileViewModel.updateUser()
                                        }
                                        if (updateUserResult != null) {
                                            mainScreenViewModel.updateUserModel(updateUserResult)
                                        }
                                    }
                                },
                                onDeleteAccount = {
                                    profileViewModel.deleteUser()
                                    onDeleteAccount()
                                }
                            )
                        }
                        else -> error("Destination not supported: $screen")
                    }
                }
            )
        }
    }

}

@Composable
private fun MainScreenContent(
    pageTitle: String = "",
    pageIcon: ImageVector? = null,
    userName: String? = null,
    userProfilePicture: ImageBitmap? = null,
    onEditProfileClicked: () -> Unit,
    currentScreen: Screen,
    getScreenContent: (@Composable (Screen) -> Unit)
) {
    val userNameDropdownExpanded = remember { mutableStateOf(false) }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(White)
        ) {
            if (pageIcon != null) {
                Icon(
                    imageVector = pageIcon,
                    contentDescription = null,
                    tint = DarkCharcoal,
                    modifier = Modifier
                        .padding(start = 16.dp)
                )
            }

            Text(
                text = pageTitle,
                style = MaterialTheme.typography.h6,
                fontSize = 26.sp,
                modifier = Modifier
                    .padding(
                        start = if (pageIcon != null) 12.dp else 16.dp,
                        top = 6.dp,
                        bottom = 6.dp
                    )
            )

            Spacer(
                modifier = Modifier
                    .weight(1f)
            )

            if (userProfilePicture != null) {
                Image(
                    bitmap = userProfilePicture,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(vertical = 6.dp)
                )
            }

            if (userName != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .clickable { userNameDropdownExpanded.value = !userNameDropdownExpanded.value }
                ) {
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.h6,
                        color = DimGray,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(
                                end = 4.dp,
                                start = 10.dp
                            )
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        tint = DimGray,
                        modifier = Modifier
                            .padding(
                                end = 6.dp
                            )
                    )
                    DropdownMenu(
                        expanded = userNameDropdownExpanded.value,
                        onDismissRequest = { userNameDropdownExpanded.value = false }
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                onEditProfileClicked()
                                userNameDropdownExpanded.value = false
                            },
                        ) {
                            Text(ResourcesUtils.Strings.EDIT_PROFILE)
                        }
                    }
                }


            }

        }

        getScreenContent(currentScreen)
    }
}

@Composable
private fun SideNavigationPanel(
    onLogoutClicked: () -> Unit,
    onItemClicked: (NavigationItem) -> Unit,
    selectedNavIndex: Int = 0,
    modifier: Modifier = Modifier
) {
    SideNavigationPanel(
        onItemClicked = onItemClicked,
        selectedIndex = selectedNavIndex,
        contentTop = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(vertical = 28.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = ResourcesUtils.Strings.COMPLETE_APP_TITLE,
                    style = MaterialTheme.typography.h4,
                    color = White,
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                )
                Divider(
                    color = White
                )
            }
        },
        contentBottom = {
            Button(
                onClick = onLogoutClicked,
                shape = RoundedCornerShape(4.dp),
                contentPadding = PaddingValues(vertical = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = DarkCornflowerBlue,
                    contentColor = White
                ),
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Outlined.Logout,
                    contentDescription = null,
                    tint = White
                )
                Text(
                    text = ResourcesUtils.Strings.LOGOUT_BUTTON,
                    style = MaterialTheme.typography.button
                )
            }
        },
        navPanelColors = NavigationPanelColors(
            backgroundColor = DarkUnbBlue,
            selectedItemColor = DarkCornflowerBlue,
            unSelectedItemColor = DarkUnbBlue,
            selectedTextColor = White,
            unSelectedTextColor = ShadowBlue,
            selectedIndicatorColor = White
        ),
        navItems = listOf(
            NavigationItem(ResourcesUtils.Strings.SUBJECTS, Icons.Outlined.School, NAV_ITEM_SUBJECTS_INDEX),
            NavigationItem(ResourcesUtils.Strings.CLASSES, Icons.Outlined.CollectionsBookmark, NAV_ITEM_CLASSES_INDEX),
            NavigationItem(ResourcesUtils.Strings.TEACHERS, Icons.Outlined.Group, NAV_ITEM_TEACHERS_INDEX),
        ),
        modifier = modifier
    )
}
