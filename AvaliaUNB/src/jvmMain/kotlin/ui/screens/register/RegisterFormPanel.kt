package ui.screens.register

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import resources.StringResources
import theme.AntiFlashWhite
import theme.DimGray
import theme.UnbGreen
import theme.White
import ui.components.PrimaryButton
import ui.components.UserFormFields
import ui.screens.register.viewmodel.RegisterFormViewModel

@Composable
fun RegisterFormPanel(
    registerFormViewModel: RegisterFormViewModel,
    onBackClicked: () -> Unit,
    onRegisterButtonClicked: () -> Unit
) {
    val registerUiState by registerFormViewModel.registerFormUiState.collectAsState()

    Box {
        val stateVertical = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(stateVertical)
        ) {
            RegisterFormTitle(onBackClicked)
            UserFormFields(
                userRegistrationNumber = registerUiState.registrationNumber,
                userName = registerUiState.name,
                userCourse = registerUiState.course ?: "",
                userEmail = registerUiState.email,
                userPassword = registerUiState.password,
                onRegistrationNumberChanged = { registerFormViewModel.updateRegistrationNumber(it) },
                onNameChanged = { registerFormViewModel.updateName(it) },
                onCourseChanged = { registerFormViewModel.updateCourse(it) },
                onEmailChanged = { registerFormViewModel.updateEmail(it) },
                onPasswordChanged = { registerFormViewModel.updatePassword(it) },
                registrationNumberAlreadyInUse = registerUiState.registrationNumberAlreadyInUse,
                emailAlreadyInUse = registerUiState.emailAlreadyInUse,
                invalidFields = registerUiState.run {
                    listOf(
                        invalidRegistrationNumber,
                        invalidName,
                        false,
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
            RegisterFormButton(onRegisterButtonClicked)
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

    Text(
        text = StringResources.REGISTER_FORM_TITLE,
        style = MaterialTheme.typography.h6,
        modifier = Modifier
            .padding(start = 32.dp, end = 32.dp, top = 40.dp)
    )
    Divider(
        modifier = Modifier
            .padding(start = 40.dp, end = 40.dp, top = 14.dp)
    )

}


@Composable
private fun RegisterFormButton(onRegisterButtonClicked: () -> Unit) {
    PrimaryButton(
        label = StringResources.REGISTER_BUTTON,
        onClick = onRegisterButtonClicked,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp, horizontal = 32.dp)
    )
}