package ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.School
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import di.DaggerComponentHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ui.components.navigation.NavigationItem
import ui.components.navigation.NavigationPanelColors
import ui.components.navigation.SideNavigationPanel
import ui.screens.classes.all.ClassesScreen
import ui.screens.classes.single.SingleClassScreen
import ui.screens.classes.single.viewmodel.SingleClassViewModel
import ui.screens.main.viewmodel.MainScreenViewModel
import ui.screens.profile.edit.EditProfileScreen
import ui.screens.profile.edit.viewmodel.ProfileViewModel
import ui.screens.profile.view.ViewProfileScreen
import ui.screens.profile.view.viewmodel.ViewProfileViewModel
import ui.screens.subjects.all.SubjectsScreen
import ui.screens.subjects.single.SingleSubjectScreen
import ui.screens.subjects.single.viewmodel.SingleSubjectViewModel
import ui.screens.teachers.all.TeachersScreen
import ui.screens.teachers.single.SingleTeacherScreen
import ui.screens.teachers.single.viewmodel.SingleTeacherViewModel
import utils.Utils
import utils.navigation.Screen
import utils.resources.Colors
import utils.resources.Strings

const val NAV_NO_SELECTED_ITEM_INDEX = -1
const val NAV_ITEM_SUBJECTS_INDEX = 0
const val NAV_ITEM_CLASSES_INDEX = 1
const val NAV_ITEM_TEACHERS_INDEX = 2
private const val USER_NAME_MAX_LENGTH = 25

@Composable
fun MainScreen(
    mainScreenViewModel: MainScreenViewModel,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    val mainScreenUiState by mainScreenViewModel.mainScreenUiState.collectAsState()

    Row {
        SideNavigationPanel(
            onLogoutClicked = onLogout,
            selectedNavIndex = when {
                mainScreenUiState.onEditProfile -> NAV_NO_SELECTED_ITEM_INDEX
                mainScreenUiState.selectedNavItemIndex == null -> {
                    mainScreenViewModel.updatePageInformation(
                        newTitle = Strings.SUBJECTS,
                        newIcon = Icons.Outlined.School
                    )
                    NAV_ITEM_SUBJECTS_INDEX
                }
                else -> mainScreenUiState.selectedNavItemIndex!!
            },
            navItems = mainScreenUiState.navItems,
            onItemClicked = { navItem ->
                mainScreenViewModel.clearItemsSelection()

                mainScreenViewModel.updateCurrentScreen(
                    when(navItem.index) {
                        NAV_ITEM_SUBJECTS_INDEX -> Screen.SUBJECTS
                        NAV_ITEM_CLASSES_INDEX -> Screen.CLASSES
                        NAV_ITEM_TEACHERS_INDEX -> Screen.TEACHERS
                        else -> Screen.SUBJECTS
                    }
                )

            },
            modifier = Modifier
                .weight(1f)
        )
        Column(
            modifier = Modifier
                .weight(4f)
        ) {
            MainScreenContent(
                pageTitle = if (mainScreenUiState.onEditProfile) {
                    Strings.PROFILE
                } else {
                    mainScreenUiState.pageTitle
                },
                pageIcon = if (mainScreenUiState.onEditProfile) {
                    Icons.Outlined.Badge
                } else {
                    mainScreenUiState.pageIcon
                },
                userName = mainScreenUiState.currentUser.name,
                userProfilePicture = mainScreenUiState.currentUser.profilePicture ?: Utils.getDefaultProfilePicture(),
                isUserAdministrator = mainScreenUiState.currentUser.isAdministrator,
                onEditProfileClicked = {
                    mainScreenViewModel.updateCurrentScreen(Screen.EDIT_PROFILE)
                    mainScreenViewModel.setIsOnProfile(true)
                },
                currentScreen = mainScreenUiState.currentScreen,
                getScreenContent = { screen ->
                    when(screen) {
                        Screen.SUBJECTS -> SubjectsScreen(
                            subjectsViewModel = DaggerComponentHolder.appComponent.getSubjectsViewModel(),
                            onSubjectClicked = { subjectModel ->
                                mainScreenViewModel.setSubjectSelection(subjectModel)
                                mainScreenViewModel.updateCurrentScreen(Screen.SINGLE_SUBJECT)
                            }
                        )
                        Screen.SINGLE_SUBJECT -> SingleSubjectScreen(
                            singleSubjectViewModel = SingleSubjectViewModel(
                               subjectModel = mainScreenUiState.selectedSubject!!,
                               subjectRepository = DaggerComponentHolder.appComponent.getSubjectRepository()
                            ),
                            onBackClicked = {
                                mainScreenViewModel.updateCurrentScreen(Screen.SUBJECTS)
                                mainScreenViewModel.setSubjectSelection(null)
                            },
                            onClassClicked = { classModel ->
                                mainScreenViewModel.updateCurrentScreen(Screen.SINGLE_CLASS)
                                mainScreenViewModel.setClassSelection(classModel)
                            }
                        )
                        Screen.CLASSES -> ClassesScreen(
                            classesViewModel = DaggerComponentHolder.appComponent.getClassesViewModel(),
                            onClassClicked = { classModel ->
                                mainScreenViewModel.updateCurrentScreen(Screen.SINGLE_CLASS)
                                mainScreenViewModel.setClassSelection(classModel)
                            }
                        )
                        Screen.SINGLE_CLASS -> SingleClassScreen(
                            singleClassViewModel = SingleClassViewModel(
                                classModel = mainScreenUiState.selectedClass!!,
                                classRepository = DaggerComponentHolder.appComponent.getClassRepository(),
                                reviewRepository = DaggerComponentHolder.appComponent.getReviewRepository(),
                                user = mainScreenUiState.currentUser,
                                userRepository = DaggerComponentHolder.appComponent.getUserRepository(),
                                reportRepository = DaggerComponentHolder.appComponent.getReportRepository()
                            ),
                            onBackClicked = {
                                mainScreenViewModel.updateCurrentScreen(
                                    if (mainScreenUiState.selectedSubject != null) {
                                        Screen.SINGLE_SUBJECT
                                    } else {
                                        Screen.CLASSES
                                    }
                                )

                                mainScreenViewModel.setClassSelection(null)
                            },
                            onSeeTeacherDetailsClicked = { teacherModel ->
                                mainScreenViewModel.updateCurrentScreen(Screen.SINGLE_TEACHER)
                                mainScreenViewModel.setTeacherSelection(teacherModel)
                            },
                            onUserClicked = { userRegistrationNumber ->
                                mainScreenViewModel.updateCurrentScreen(Screen.VIEW_PROFILE)
                                mainScreenViewModel.setUserSelection(userRegistrationNumber)
                            }
                        )
                        Screen.TEACHERS -> TeachersScreen(
                            teachersViewModel = DaggerComponentHolder.appComponent.getTeachersViewModel(),
                            onTeacherClicked = { teacherModel ->
                                mainScreenViewModel.updateCurrentScreen(Screen.SINGLE_TEACHER)
                                mainScreenViewModel.setTeacherSelection(teacherModel)
                            }
                        )
                        Screen.SINGLE_TEACHER -> SingleTeacherScreen(
                            singleTeacherViewModel = SingleTeacherViewModel(
                                teacherModel = mainScreenUiState.selectedTeacher!!,
                                teacherRepository = DaggerComponentHolder.appComponent.getTeacherRepository(),
                                reviewRepository = DaggerComponentHolder.appComponent.getReviewRepository(),
                                user = mainScreenUiState.currentUser,
                                userRepository = DaggerComponentHolder.appComponent.getUserRepository(),
                                reportRepository = DaggerComponentHolder.appComponent.getReportRepository()
                            ),
                            onBackClicked = {
                                mainScreenViewModel.updateCurrentScreen(
                                    if (mainScreenUiState.selectedClass != null) {
                                        Screen.SINGLE_CLASS
                                    } else {
                                        Screen.TEACHERS
                                    }
                                )

                                mainScreenViewModel.setTeacherSelection(null)
                            },
                            onUserClicked = { userRegistrationNumber ->
                                mainScreenViewModel.updateCurrentScreen(Screen.VIEW_PROFILE)
                                mainScreenViewModel.setUserSelection(userRegistrationNumber)
                            }
                        )
                        Screen.VIEW_PROFILE -> {
                            mainScreenViewModel.setIsOnProfile(true)

                            val viewProfileViewModel = ViewProfileViewModel(
                                    userRegistrationNumber = mainScreenUiState.selectedUserRegistrationNumber!!,
                                    userRepository = DaggerComponentHolder.appComponent.getUserRepository(),
                                    reportRepository = DaggerComponentHolder.appComponent.getReportRepository(),
                                    reviewRepository = DaggerComponentHolder.appComponent.getReviewRepository()
                                )

                            val onBackClicked = {
                                mainScreenViewModel.updateSelectedItemsScore()

                                mainScreenViewModel.updateCurrentScreen(
                                    if (mainScreenUiState.selectedTeacher != null) {
                                        Screen.SINGLE_TEACHER
                                    } else {
                                        Screen.SINGLE_CLASS
                                    }
                                )
                                mainScreenViewModel.setUserSelection(null)
                            }

                            ViewProfileScreen(
                                viewProfileViewModel = viewProfileViewModel,
                                onDeleteAccount = {
                                    viewProfileViewModel.deleteUser()

                                    onBackClicked()
                                },
                                onBackClicked = onBackClicked
                            )
                        }
                        Screen.EDIT_PROFILE -> {
                            val profileViewModel = ProfileViewModel(
                                mainScreenUiState.currentUser,
                                DaggerComponentHolder.appComponent.getUserRepository()
                            )

                            EditProfileScreen(
                                profileViewModel = profileViewModel,
                                onBackClicked = {
                                    mainScreenViewModel.setIsOnProfile(false)
                                    mainScreenViewModel.updateCurrentScreen(
                                        when (mainScreenUiState.selectedNavItemIndex) {
                                            null, NAV_ITEM_SUBJECTS_INDEX -> Screen.SUBJECTS
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
    userName: String,
    userProfilePicture: ImageBitmap,
    isUserAdministrator: Boolean = false,
    onEditProfileClicked: () -> Unit,
    currentScreen: Screen,
    getScreenContent: (@Composable (Screen) -> Unit)
) {
    val userNameDropdownExpanded = remember { mutableStateOf(false) }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Colors.White)
        ) {
            if (pageIcon != null) {
                Icon(
                    imageVector = pageIcon,
                    contentDescription = null,
                    tint = Colors.DarkCharcoal,
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

            if (isUserAdministrator) AdministratorBadge()
            
            Image(
                bitmap = userProfilePicture,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(40.dp)
                    .padding(top = 6.dp, bottom = 6.dp, start = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .clickable { userNameDropdownExpanded.value = !userNameDropdownExpanded.value }
            ) {
                UserName(userName)

                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    tint = Colors.DimGray
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
                        Text(Strings.ACTION_EDIT_PROFILE)
                    }
                }
            }
        }

        getScreenContent(currentScreen)
    }
}

@Composable
private fun AdministratorBadge() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(percent = 50))
            .background(Colors.UnbGreen)
            .padding(vertical = 4.dp, horizontal = 12.dp)
    ) {
        Text(
            text = Strings.ADMINISTRATOR,
            style = MaterialTheme.typography.subtitle1,
            color = Colors.White
        )
    }
}

@Composable
private fun UserName(
    userName: String
) {
    val nameSplit = userName.split(" ")

    var name = ""

    for (part in nameSplit) {
        name += if ("$name$part".length <= USER_NAME_MAX_LENGTH) "$part " else ""
    }

    if (name.isEmpty()) {
        name = nameSplit[0].substring(0, USER_NAME_MAX_LENGTH) + "..."
    }

    Text(
        text = name,
        style = MaterialTheme.typography.h6,
        color = Colors.DimGray,
        fontSize = 24.sp,
        modifier = Modifier
            .padding(start = 10.dp)
    )
}



@Composable
private fun SideNavigationPanel(
    onLogoutClicked: () -> Unit,
    onItemClicked: (NavigationItem) -> Unit,
    selectedNavIndex: Int = 0,
    navItems: List<NavigationItem>,
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
                    text = Strings.APP_TITLE_COMPLETE,
                    style = MaterialTheme.typography.h4,
                    color = Colors.White,
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                )
                Divider(
                    color = Colors.White
                )
            }
        },
        contentBottom = {
            Button(
                onClick = onLogoutClicked,
                shape = RoundedCornerShape(4.dp),
                contentPadding = PaddingValues(vertical = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Colors.DarkCornflowerBlue,
                    contentColor = Colors.White
                ),
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Outlined.Logout,
                    contentDescription = null,
                    tint = Colors.White
                )
                Text(
                    text = Strings.LOGOUT,
                    style = MaterialTheme.typography.button
                )
            }
        },
        navPanelColors = NavigationPanelColors(
            backgroundColor = Colors.UnbBlueDark,
            selectedItemColor = Colors.DarkCornflowerBlue,
            unSelectedItemColor = Colors.UnbBlueDark,
            selectedTextColor = Colors.White,
            unSelectedTextColor = Colors.ShadowBlue,
            selectedIndicatorColor = Colors.White
        ),
        navItems = navItems,
        modifier = modifier
    )
}
