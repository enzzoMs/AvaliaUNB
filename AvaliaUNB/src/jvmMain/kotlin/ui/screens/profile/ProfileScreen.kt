package ui.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import utils.resources.ResourcesUtils
import theme.*
import ui.components.buttons.PrimaryButton
import ui.components.buttons.SecondaryButton
import ui.components.forms.UserFormFields
import ui.screens.profile.viewmodel.ProfileViewModel

private const val NUMBER_OF_FIELDS = 5

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    onBackClicked: () -> Unit,
    onFinishEditClicked: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    val profileUiState by profileViewModel.profileUiState.collectAsState()

    Box {
        val stateVertical = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(DarkAntiFlashWhite)
                .verticalScroll(stateVertical)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    AnimatedVisibility(profileUiState.isEditingFields) {
                        DeleteAccountButton(
                            onConfirmButtonClicked = onDeleteAccount
                        )
                    }
                    UserProfilePicture(
                        userProfilePicture = profileUiState.profilePic,
                        editButtonEnabled = profileUiState.isEditingFields,
                        removePictureButtonEnabled = profileUiState.showRemovePictureButton,
                        onEditClicked = {
                            CoroutineScope(Dispatchers.IO).launch {
                                profileViewModel.editUserProfilePicture()
                            }
                        },
                        onRemoveClicked = {
                            CoroutineScope(Dispatchers.IO).launch {
                                profileViewModel.removeProfilePicture()
                            }
                        }
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    UserFormFields(
                        userRegistrationNumber = profileUiState.registrationNumber,
                        userName = profileUiState.name,
                        userCourse = profileUiState.course ?: "",
                        userEmail = profileUiState.email,
                        userPassword = profileUiState.password,
                        onRegistrationNumberChanged = { profileViewModel.updateRegistrationNumber(it) },
                        onNameChanged = { profileViewModel.updateName(it) },
                        onCourseChanged = { profileViewModel.updateCourse(it) },
                        onEmailChanged = { profileViewModel.updateEmail(it) },
                        onPasswordChanged = { profileViewModel.updatePassword(it) },
                        registrationNumberAlreadyInUse = profileUiState.registrationNumberAlreadyInUse,
                        emailAlreadyInUse = profileUiState.emailAlreadyInUse,
                        invalidFields = profileUiState.run {
                            listOf(
                                invalidRegistrationNumber,
                                invalidName,
                                false,
                                invalidEmail,
                                invalidPassword
                            )
                        },
                        enabledFields = List(NUMBER_OF_FIELDS) { profileUiState.isEditingFields },
                        backgroundColor = Platinum,
                        modifier = Modifier
                            .padding(top = 30.dp)
                    )
                }
            }

            if (profileUiState.isEditingFields) {
                FinishAndCancelButtons(
                    onCancelEditClicked = { profileViewModel.cancelEditingUser() },
                    onFinishEditClicked = onFinishEditClicked
                )
            } else {
                EditAndBackButtons(
                    onBackClicked = onBackClicked,
                    onEditClicked = { profileViewModel.setIsEditingFields(true) }
                )
            }
        }

        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(stateVertical),
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
        )
    }

}

@Composable
private fun DeleteAccountButton(
    onConfirmButtonClicked: () -> Unit = {}
) {
    var confirmationVisible by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(bottom = 30.dp, top = 52.dp)
    ) {
        Button(
            onClick = { confirmationVisible = true } ,
            contentPadding = PaddingValues(vertical = 10.dp, horizontal = 15.dp),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                hoveredElevation = 0.dp,
                focusedElevation = 0.dp
            ),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.error,
                contentColor = White
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = null,
                tint = White,
                modifier = Modifier
                    .padding(end = 8.dp)
            )
            Text(
                text = ResourcesUtils.Strings.DELETE_ACCOUNT_BUTTON,
                style = MaterialTheme.typography.button,
                color = White
            )
        }

        AnimatedVisibility(confirmationVisible) {
            Row {
                Button(
                    onClick = { confirmationVisible = false } ,
                    contentPadding = PaddingValues(vertical = 10.dp, horizontal = 15.dp),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        hoveredElevation = 0.dp,
                        focusedElevation = 0.dp
                    ),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = White
                    ),
                    modifier = Modifier
                        .padding(start = 24.dp)
                ) {
                    Text(
                        text = ResourcesUtils.Strings.CANCEL_BUTTON_LOWERCASE,
                        style = MaterialTheme.typography.button,
                        color = DimGray
                    )
                }

                Button(
                    onClick = onConfirmButtonClicked,
                    contentPadding = PaddingValues(vertical = 10.dp, horizontal = 15.dp),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        hoveredElevation = 0.dp,
                        focusedElevation = 0.dp
                    ),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.error,
                        contentColor = White
                    ),
                    modifier = Modifier
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = ResourcesUtils.Strings.CONFIRM_BUTTON,
                        style = MaterialTheme.typography.button,
                        color = White
                    )
                }
            }
        }
    }
}


@Composable
private fun UserProfilePicture(
    userProfilePicture: ImageBitmap,
    editButtonEnabled: Boolean = false,
    removePictureButtonEnabled: Boolean = false,
    onEditClicked: () -> Unit = {},
    onRemoveClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .then(modifier)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(horizontal = 60.dp)
                .size(260.dp)
        ) {
            Image(
                bitmap = userProfilePicture,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(250.dp)
            )
            if (removePictureButtonEnabled) {
                IconButton(
                    onClick = onRemoveClicked,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.error)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = null,
                        tint = White
                    )
                }
            }
        }


        AnimatedVisibility(editButtonEnabled) {
            Button(
                onClick = onEditClicked,
                contentPadding = PaddingValues(10.dp),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    hoveredElevation = 0.dp,
                    focusedElevation = 0.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = DarkAntiFlashWhite
                ),
                modifier = Modifier
                    .padding(top = 30.dp, end = 60.dp, start = 60.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .border(
                        width = 2.dp,
                        color = Platinum,
                        shape = RoundedCornerShape(4.dp),
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.Image,
                    contentDescription = null,
                    tint = Gray,
                    modifier = Modifier
                        .padding(end = 8.dp)
                )
                Text(
                    text = ResourcesUtils.Strings.PICK_PROFILE_PIC_BUTTON,
                    style = MaterialTheme.typography.button,
                    color = Gray
                )
            }
        }
    }
}


@Composable
private fun EditAndBackButtons(
    onBackClicked: () -> Unit,
    onEditClicked: () -> Unit
) {
    Row {
        PrimaryButton(
            label = ResourcesUtils.Strings.EDIT_PROFILE_BUTTON,
            onClick = onEditClicked,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 20.dp)
        )

        SecondaryButton(
            label = ResourcesUtils.Strings.BACK_BUTTON,
            onClick = onBackClicked,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 20.dp)
        )
    }

}

@Composable
private fun FinishAndCancelButtons(
    onFinishEditClicked: () -> Unit,
    onCancelEditClicked: () -> Unit
) {
    Row {
        PrimaryButton(
            label = ResourcesUtils.Strings.FINISH_BUTTON,
            onClick = onFinishEditClicked,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 20.dp)
        )

        SecondaryButton(
            label = ResourcesUtils.Strings.CANCEL_BUTTON_UPPERCASE,
            onClick = onCancelEditClicked,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 20.dp)
        )
    }

}