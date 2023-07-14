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
import theme.*
import ui.components.buttons.PrimaryButton
import ui.components.buttons.SecondaryButton
import ui.components.forms.UserFormFields
import ui.screens.profile.viewmodel.ProfileViewModel
import utils.resources.Colors
import utils.resources.Strings

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
                .background(Colors.DarkAntiFlashWhite)
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
                        backgroundColor = Colors.Platinum,
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
                contentColor = Colors.White
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = null,
                tint = Colors.White,
                modifier = Modifier
                    .padding(end = 8.dp)
            )
            Text(
                text = Strings.ACTION_DELETE_ACCOUNT,
                style = MaterialTheme.typography.button,
                color = Colors.White
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
                        backgroundColor = Colors.White
                    ),
                    modifier = Modifier
                        .padding(start = 24.dp)
                ) {
                    Text(
                        text = Strings.CANCEL,
                        style = MaterialTheme.typography.button,
                        color = Colors.DimGray
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
                        contentColor = Colors.White
                    ),
                    modifier = Modifier
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = Strings.CONFIRM,
                        style = MaterialTheme.typography.button,
                        color = Colors.White
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
                contentScale = ContentScale.Fit,
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
                        tint = Colors.White
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
                    backgroundColor = Colors.DarkAntiFlashWhite
                ),
                modifier = Modifier
                    .padding(top = 30.dp, end = 60.dp, start = 60.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .border(
                        width = 2.dp,
                        color = Colors.Platinum,
                        shape = RoundedCornerShape(4.dp),
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.Image,
                    contentDescription = null,
                    tint = Colors.Gray,
                    modifier = Modifier
                        .padding(end = 8.dp)
                )
                Text(
                    text = Strings.ACTION_PICK_IMAGE,
                    style = MaterialTheme.typography.button,
                    color = Colors.Gray
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
            label = Strings.CAPITALIZED_EDIT,
            onClick = onEditClicked,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 20.dp)
        )

        SecondaryButton(
            label = Strings.CAPITALIZED_BACK,
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
            label = Strings.CAPITALIZED_FINISH,
            onClick = onFinishEditClicked,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 20.dp)
        )

        SecondaryButton(
            label = Strings.CAPITALIZED_CANCEL,
            onClick = onCancelEditClicked,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 20.dp)
        )
    }

}