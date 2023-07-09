package ui.screens.login

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import utils.resources.ResourcesUtils
import theme.UnbGreen
import ui.components.forms.FormField
import ui.components.forms.GeneralTextField
import ui.components.buttons.PrimaryButton
import ui.components.buttons.SecondaryButton
import ui.screens.login.viewmodel.LoginViewModel
import java.awt.Cursor


@Composable
fun LoginFormPanel(
    loginViewModel: LoginViewModel,
    onLoginButtonClicked: () -> Unit,
    onRegisterClicked: () -> Unit
) {
    val loginUiState by loginViewModel.loginUiState.collectAsState()

    Box {
        val stateVertical = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(start = 32.dp, end = 32.dp, top = 32.dp)
                .fillMaxHeight()
                .verticalScroll(stateVertical)
        ) {
            LoginFormTitle()
            LoginFormFields(
                userRegistrationNumber = loginUiState.registrationNumber,
                userPassword = loginUiState.password,
                onRegistrationNumberChanged = { loginViewModel.updateRegistrationNumber(it) },
                onPasswordChanged = { loginViewModel.updatePassword(it) },
                invalidRegistrationNumber= loginUiState.invalidRegistrationNumber,
                invalidPassword = loginUiState.invalidPassword,
                userNotRegistered = loginUiState.userNotRegistered,
                wrongPassword = loginUiState.wrongPassword,
                onLoginButtonClicked = onLoginButtonClicked
            )
            Spacer(
                modifier = Modifier
                    .weight(1f)
            )
            LoginRegisterText(onRegisterClicked)
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
private fun LoginFormTitle() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image (
            painter = painterResource(ResourcesUtils.ImagePaths.APP_LOGO),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(90.dp)
        )
        Text(
            textAlign = TextAlign.Center,
            text = ResourcesUtils.Strings.LOGIN_TITLE,
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .padding(start = 26.dp)
        )
    }
    Text(
        text = ResourcesUtils.Strings.LOGIN_SUBTITLE,
        textAlign = TextAlign.Justify,
        style = MaterialTheme.typography.subtitle2,
        modifier = Modifier
            .padding(top = 20.dp, bottom = 24.dp)
    )
}

@Composable
private fun LoginFormFields(
    userRegistrationNumber: String,
    userPassword: String,
    onRegistrationNumberChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    invalidRegistrationNumber: Boolean = false,
    invalidPassword: Boolean = false,
    userNotRegistered: Boolean = false,
    wrongPassword: Boolean = false,
    onLoginButtonClicked: () -> Unit
) {

    Text(
        text = ResourcesUtils.Strings.LOGIN_FORM_TITLE,
        style = MaterialTheme.typography.h6
    )

    Divider(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 14.dp, bottom = 16.dp)
    )

    // Registration Number Field -------------------------------------------------------
    FormField(
        title = ResourcesUtils.Strings.REGISTRATION_NUMBER_FIELD_TITLE,
        error = invalidRegistrationNumber || userNotRegistered,
        errorMessage = if (userNotRegistered) {
            ResourcesUtils.Strings.USER_NOT_REGISTERED
        } else {
            ResourcesUtils.Strings.INVALID_REGISTRATION_NUMBER
        },
        textField = {
            GeneralTextField(
                value = userRegistrationNumber,
                onValueChange = onRegistrationNumberChanged,
                error = invalidRegistrationNumber || userNotRegistered,
                hintText = ResourcesUtils.Strings.REGISTRATION_NUMBER_FIELD_HINT,
                startIcon = Icons.Outlined.Badge
            )
        }
    )

    // Password Field -------------------------------------------------------
    FormField(
        title = ResourcesUtils.Strings.PASSWORD_FIELD_TITLE,
        error = invalidPassword || wrongPassword,
        errorMessage = if (wrongPassword) {
            ResourcesUtils.Strings.WRONG_PASSWORD
        } else {
            ResourcesUtils.Strings.REQUIRED_FIELD
        },
        modifier = Modifier
            .padding(top = 22.dp),
        textField = {
            var passwordFieldEndIcon by remember { mutableStateOf(Icons.Filled.VisibilityOff) }
            var passwordFieldVisualTransformation: VisualTransformation by remember {
                    mutableStateOf(PasswordVisualTransformation()
                )
            }

            GeneralTextField(
                value = userPassword,
                onValueChange = onPasswordChanged,
                error = invalidPassword || wrongPassword,
                hintText = ResourcesUtils.Strings.PASSWORD_FIELD_HINT,
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

    PrimaryButton(
        label = ResourcesUtils.Strings.LOGIN_BUTTON,
        onClick = onLoginButtonClicked,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 36.dp)
    )

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 22.dp)
                .weight(1f)
        )
        Text(
            text = ResourcesUtils.Strings.GENERAL_TEXT_OR,
            style = MaterialTheme.typography.subtitle2
        )
        Divider(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .weight(1f)
        )
    }

    SecondaryButton(
        label = ResourcesUtils.Strings.LOGIN_WITHOUT_ACCOUNT_BUTTON,
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
private fun LoginRegisterText(
    onRegisterClicked: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(bottom = 40.dp, top = 26.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = ResourcesUtils.Strings.LOGIN_NO_ACCOUNT_QUESTION,
            style = MaterialTheme.typography.body2,
            modifier = Modifier
                .padding(end = 6.dp)
        )
        Text(
            text = ResourcesUtils.Strings.LOGIN_NO_ACCOUNT_REGISTER,
            style = MaterialTheme.typography.body2,
            color = UnbGreen,
            modifier = Modifier
                .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)))
                .clickable(
                    onClick = onRegisterClicked,
                    indication = null,
                    interactionSource = MutableInteractionSource()
                )
        )
    }
}












