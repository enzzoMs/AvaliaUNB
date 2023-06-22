package ui.screens.register

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import resources.StringResources
import theme.*
import ui.components.FormField
import ui.components.GeneralTextField
import ui.screens.register.viewmodel.RegisterViewModel

const val REGISTRATION_NUMBER_FIELD_INDEX = 0
const val NAME_FIELD_INDEX = 1
const val EMAIL_FIELD_INDEX = 2
const val PASSWORD_FIELD_INDEX = 3

@Composable
fun RegisterFormPanel(
    registerViewModel: RegisterViewModel
) {
    val registerUiState by registerViewModel.registerUiState.collectAsState()

    Box {
        val stateVertical = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(stateVertical)
        ) {
            RegisterFormTitle { registerViewModel.navigateBack() }
            RegisterFormFields(
                userRegistrationNumber = registerUiState.registrationNumber,
                onRegistrationNumberChanged = { registerViewModel.updateRegistrationNumber(it) },
                userName = registerUiState.name,
                onNameChanged = { registerViewModel.updateName(it) },
                userCourse = registerUiState.course ?: "",
                onCourseChanged = { registerViewModel.updateCourse(it) },
                userEmail = registerUiState.email,
                onEmailChanged = { registerViewModel.updateEmail(it) },
                userPassword = registerUiState.password,
                onPasswordChanged = { registerViewModel.updatePassword(it) },
                registrationNumberAlreadyInUse = registerUiState.registrationNumberAlreadyInUse,
                emailAlreadyInUse = registerUiState.emailAlreadyInUse,
                invalidFields = registerUiState.run {
                    listOf(
                        invalidRegistrationNumber,
                        invalidName,
                        invalidEmail,
                        invalidPassword
                    )
                }
            )
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Red)
            )
            RegisterFormButton { registerViewModel.registerUser() }
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
private fun RegisterFormTitle(
    onBackClicked: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 22.dp)
    ) {
        Button(
            onClick = onBackClicked,
            shape = RectangleShape,
            contentPadding = PaddingValues(start = 38.dp, end = 24.dp, top = 10.dp, bottom = 10.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = UnbGreen,
                contentColor = White
            ),
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIos,
                contentDescription = null,
                tint = White
            )
        }

        Spacer(
            modifier = Modifier
                .weight(1f)
        )

        Box(
            modifier = Modifier
                .padding(end = 32.dp)
                .clip(CircleShape)
                .background(AntiFlashWhite)
        ) {
            Icon(
                imageVector = Icons.Outlined.PersonAdd,
                contentDescription = null,
                tint = DimGray,
                modifier = Modifier
                    .size(60.dp)
                    .padding(10.dp)
                    .clip(CircleShape)
            )
        }
    }
}

@Composable
private fun RegisterFormFields(
    userRegistrationNumber: String,
    onRegistrationNumberChanged: (String) -> Unit,
    userName: String,
    onNameChanged: (String) -> Unit,
    userCourse: String,
    onCourseChanged: (String) -> Unit,
    userEmail: String,
    onEmailChanged: (String) -> Unit,
    userPassword: String,
    onPasswordChanged: (String) -> Unit,
    registrationNumberAlreadyInUse: Boolean = false,
    emailAlreadyInUse: Boolean = false,
    invalidFields: List<Boolean>
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(start = 32.dp, end = 32.dp, top = 40.dp)
    ) {
        Text(
            text = StringResources.REGISTER_FORM_TITLE,
            style = MaterialTheme.typography.h6
        )
        Divider(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, top = 14.dp, bottom = 16.dp)
        )

        // Registration Number Field -----------------------------------------
        FormField(
            title = StringResources.REGISTRATION_NUMBER_FIELD_TITLE,
            error = invalidFields[REGISTRATION_NUMBER_FIELD_INDEX] || registrationNumberAlreadyInUse,
            errorMessage = if (registrationNumberAlreadyInUse) {
                StringResources.REGISTRATION_NUMBER_ALREADY_IN_USE
            } else {
                StringResources.INVALID_REGISTRATION_NUMBER
            },
            textField = {
                GeneralTextField(
                    value = userRegistrationNumber,
                    onValueChange = onRegistrationNumberChanged,
                    error = invalidFields[REGISTRATION_NUMBER_FIELD_INDEX] || registrationNumberAlreadyInUse,
                    hintText = StringResources.REGISTRATION_NUMBER_FIELD_HINT,
                    startIcon = Icons.Outlined.Badge
                )
            }
        )


        // Name Field -------------------------------------------------------
        FormField(
            title = StringResources.NAME_FIELD_TITLE,
            error = invalidFields[NAME_FIELD_INDEX],
            errorMessage = StringResources.REQUIRED_FIELD,
            modifier = Modifier
                .padding(top = 22.dp),
            textField = {
                GeneralTextField(
                    value = userName,
                    onValueChange = onNameChanged,
                    error = invalidFields[NAME_FIELD_INDEX],
                    hintText = StringResources.NAME_FIELD_HINT,
                    startIcon = Icons.Filled.TextFields
                )
            }
        )

        // Course Field -------------------------------------------------------
        FormField(
            title = StringResources.COURSE_FIELD_TITLE,
            optional = true,
            errorMessage = StringResources.REQUIRED_FIELD,
            modifier = Modifier
                .padding(top = 22.dp),
            textField = {
                GeneralTextField(
                    value = userCourse,
                    onValueChange = onCourseChanged,
                    hintText = StringResources.COURSE_FIELD_HINT,
                    startIcon = Icons.Outlined.CollectionsBookmark
                )
            }
        )

        // Email Field -------------------------------------------------------
        FormField(
            title = StringResources.EMAIL_FIELD_TITLE,
            error = invalidFields[EMAIL_FIELD_INDEX] || emailAlreadyInUse,
            errorMessage = if (emailAlreadyInUse) {
                StringResources.EMAIL_ALREADY_IN_USE
            } else {
                StringResources.REQUIRED_FIELD
            },
            modifier = Modifier
                .padding(top = 22.dp),
            textField = {
                GeneralTextField(
                    value = userEmail,
                    onValueChange = onEmailChanged,
                    error = invalidFields[EMAIL_FIELD_INDEX] || emailAlreadyInUse,
                    hintText = StringResources.EMAIL_FIELD_HINT,
                    startIcon = Icons.Filled.Email
                )
            }
        )

        // Password Field -------------------------------------------------------
        FormField(
            title = StringResources.PASSWORD_FIELD_TITLE,
            error = invalidFields[PASSWORD_FIELD_INDEX],
            errorMessage = StringResources.REQUIRED_FIELD,
            modifier = Modifier
                .padding(top = 22.dp),
            textField = {
                var passwordFieldEndIcon by remember { mutableStateOf(Icons.Filled.VisibilityOff) }
                var passwordFieldVisualTransformation: VisualTransformation by remember { mutableStateOf(
                        PasswordVisualTransformation()
                    )
                }

                GeneralTextField(
                    value = userPassword,
                    onValueChange = onPasswordChanged,
                    error = invalidFields[PASSWORD_FIELD_INDEX],
                    hintText = StringResources.PASSWORD_FIELD_HINT,
                    startIcon = Icons.Filled.Lock,
                    endIcon = passwordFieldEndIcon,
                    visualTransformation = passwordFieldVisualTransformation,
                    onEndIconClicked = {
                        if (passwordFieldVisualTransformation == VisualTransformation.None) {
                            passwordFieldVisualTransformation = PasswordVisualTransformation()
                            passwordFieldEndIcon = Icons.Filled.VisibilityOff
                        } else {
                            passwordFieldVisualTransformation = VisualTransformation.None
                            passwordFieldEndIcon = Icons.Filled.Visibility
                        }
                    }
                )
            }
        )
    }
}

@Composable
private fun RegisterFormButton(onRegisterButtonClicked: () -> Unit) {
    Button(
        onClick = onRegisterButtonClicked,
        shape = RoundedCornerShape(4.dp),
        contentPadding = PaddingValues(vertical = 11.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = UnbGreen,
            contentColor = White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp, horizontal = 32.dp)
    ) {
        Text(
            text = StringResources.REGISTER_BUTTON,
            style = MaterialTheme.typography.button
        )
    }
}