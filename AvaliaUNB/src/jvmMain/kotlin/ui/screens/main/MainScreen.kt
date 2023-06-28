package ui.screens.main

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.models.UserModel
import resources.StringResources
import theme.*
import ui.components.NavigationItem
import ui.components.NavigationPanelColors
import ui.components.SideNavigationPanel
import ui.screens.classes.ClassesScreen
import ui.screens.subjects.SubjectsScreen
import ui.screens.main.viewmodel.MainScreenViewModel
import ui.screens.teachers.TeachersScreen
import utils.navigation.NavigationComponent
import utils.navigation.NavigationController
import utils.navigation.Screen

const val NAV_ITEM_SUBJECTS_INDEX = 0
const val NAV_ITEM_CLASSES_INDEX = 1
const val NAV_ITEM_TEACHERS_INDEX = 2

@Composable
fun MainScreen(
    mainScreenViewModel: MainScreenViewModel,
    userModel: UserModel? = null,
    onLogout: () -> Unit
) {
    val navigationController = remember { NavigationController(startDestination = Screen.SUBJECTS) }

    val mainScreenUiState by mainScreenViewModel.mainScreenUiState.collectAsState()

    Row {
        SideNavigationPanel(
            onLogoutClicked = onLogout,
            selectedNavIndex = if (mainScreenUiState.currentSelectedNavItem == null) {
                mainScreenViewModel.updateNavItem(
                    NavigationItem(
                        label = StringResources.SUBJECTS,
                        icon = Icons.Outlined.School,
                        index = NAV_ITEM_SUBJECTS_INDEX
                    )
                )
                NAV_ITEM_SUBJECTS_INDEX
            } else {
                mainScreenUiState.currentSelectedNavItem!!.index
            },
            onItemClicked = { navItem ->
                mainScreenViewModel.updateNavItem(navItem)
                navigationController.navigateTo(
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
                pageTitle = mainScreenUiState.currentSelectedNavItem?.label ?: "",
                pageIcon = mainScreenUiState.currentSelectedNavItem?.icon,
                navigationController = navigationController,
                userName = userModel?.name,
                getDestination = { destination ->
                    when(destination) {
                        Screen.SUBJECTS -> SubjectsScreen()
                        Screen.CLASSES -> ClassesScreen()
                        Screen.TEACHERS -> TeachersScreen()
                        else -> error("Destination not supported: $destination")
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
    userProfilePicture: ImageVector? = null,
    navigationController: NavigationController,
    getDestination: (@Composable (destination: Screen) -> Unit)
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
                            onClick = {},
                        ) {
                            Text(StringResources.EDIT_PROFILE)
                        }
                    }
                }


            }

        }

        NavigationComponent(
            navigationController = navigationController,
            getDestination = getDestination,
            modifier = Modifier
                .weight(1f)
        )
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
                    text = StringResources.COMPLETE_APP_TITLE,
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
                    text = StringResources.LOGOUT,
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
            NavigationItem(StringResources.SUBJECTS, Icons.Outlined.School, NAV_ITEM_SUBJECTS_INDEX),
            NavigationItem(StringResources.CLASSES, Icons.Outlined.CollectionsBookmark, NAV_ITEM_CLASSES_INDEX),
            NavigationItem(StringResources.TEACHERS, Icons.Outlined.Group, NAV_ITEM_TEACHERS_INDEX),
        ),
        modifier = modifier
    )
}
