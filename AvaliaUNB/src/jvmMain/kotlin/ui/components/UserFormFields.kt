package ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import resources.StringResources
import theme.AntiFlashWhite
import theme.LightGray


private const val NUMBER_OF_FIELDS = 5
private const val REGISTRATION_NUMBER_FIELD_INDEX = 0
private const val NAME_FIELD_INDEX = 1
private const val COURSE_FIELD_INDEX = 2
private const val EMAIL_FIELD_INDEX = 3
private const val PASSWORD_FIELD_INDEX = 4

@Composable
fun UserFormFields(
    userRegistrationNumber: String,
    userName: String,
    userCourse: String,
    userEmail: String,
    userPassword: String,
    onRegistrationNumberChanged: (String) -> Unit,
    onNameChanged: (String) -> Unit,
    onCourseChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    registrationNumberAlreadyInUse: Boolean = false,
    emailAlreadyInUse: Boolean = false,
    invalidFields: List<Boolean> = List(NUMBER_OF_FIELDS) { false },
    enabledFields: List<Boolean> = List(NUMBER_OF_FIELDS) { true },
    backgroundColor: Color = AntiFlashWhite,
    unableBackgroundColor: Color = LightGray,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(start = 32.dp, end = 32.dp, top = 22.dp)
            .then(modifier)
    ) {

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
                    enabled = enabledFields[REGISTRATION_NUMBER_FIELD_INDEX],
                    error = invalidFields[REGISTRATION_NUMBER_FIELD_INDEX] || registrationNumberAlreadyInUse,
                    hintText = StringResources.REGISTRATION_NUMBER_FIELD_HINT,
                    backgroundColor = if (enabledFields[REGISTRATION_NUMBER_FIELD_INDEX]) {
                        backgroundColor
                    } else {
                        unableBackgroundColor
                    },
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
                    enabled = enabledFields[NAME_FIELD_INDEX],
                    error = invalidFields[NAME_FIELD_INDEX],
                    hintText = StringResources.NAME_FIELD_HINT,
                    backgroundColor = if (enabledFields[REGISTRATION_NUMBER_FIELD_INDEX]) {
                        backgroundColor
                    } else {
                        unableBackgroundColor
                    },
                    startIcon = Icons.Filled.TextFields
                )
            }
        )

        // Course Field -------------------------------------------------------
        FormField(
            title = StringResources.COURSE_FIELD_TITLE,
            optional = true,
            error = invalidFields[COURSE_FIELD_INDEX],
            modifier = Modifier
                .padding(top = 22.dp),
            textField = {
                GeneralTextField(
                    value = userCourse,
                    onValueChange = onCourseChanged,
                    enabled = enabledFields[COURSE_FIELD_INDEX],
                    error = invalidFields[COURSE_FIELD_INDEX],
                    hintText = StringResources.COURSE_FIELD_HINT,
                    backgroundColor = if (enabledFields[REGISTRATION_NUMBER_FIELD_INDEX]) {
                        backgroundColor
                    } else {
                        unableBackgroundColor
                    },
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
                    enabled = enabledFields[EMAIL_FIELD_INDEX],
                    error = invalidFields[EMAIL_FIELD_INDEX] || emailAlreadyInUse,
                    hintText = StringResources.EMAIL_FIELD_HINT,
                    backgroundColor = if (enabledFields[REGISTRATION_NUMBER_FIELD_INDEX]) {
                        backgroundColor
                    } else {
                        unableBackgroundColor
                    },
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
                    enabled = enabledFields[PASSWORD_FIELD_INDEX],
                    hintText = StringResources.PASSWORD_FIELD_HINT,
                    startIcon = Icons.Filled.Lock,
                    endIcon = passwordFieldEndIcon,
                    backgroundColor = if (enabledFields[REGISTRATION_NUMBER_FIELD_INDEX]) {
                        backgroundColor
                    } else {
                        unableBackgroundColor
                    },
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