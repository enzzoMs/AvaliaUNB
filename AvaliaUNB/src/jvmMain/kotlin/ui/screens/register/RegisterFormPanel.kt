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
import ui.components.GeneralTextField
import utils.navigation.NavigationController
import java.util.*

@Composable
fun RegisterFormPanel(navigationController: NavigationController) {
    Box {
        val stateVertical = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(stateVertical)
        ) {
            RegisterFormTitle(navigationController)
            RegisterFormFields()
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Red)
            )
            RegisterFormButton()
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
private fun RegisterFormTitle(navigationController: NavigationController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 22.dp)
    ) {
        Button(
            onClick = { navigationController.navigateTo(Screen.Login.label) },
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
private fun RegisterFormFields() {
    var userRegistrationNumber by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var userCourse by remember { mutableStateOf("") }

    var userPassword by remember { mutableStateOf("") }
    var passwordFieldEndIcon by remember { mutableStateOf(Icons.Filled.VisibilityOff) }
    var passwordFieldVisualTransformation: VisualTransformation by remember { mutableStateOf(
        PasswordVisualTransformation()
    ) }

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
        Text(
            text = StringResources.REGISTRATION_NUMBER_FIELD_TITLE,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .padding(bottom = 12.dp)
        )
        GeneralTextField(
            value = userRegistrationNumber,
            onValueChange = { userRegistrationNumber = it },
            hintText = StringResources.REGISTRATION_NUMBER_FIELD_HINT,
            startIcon = Icons.Outlined.Badge
        )

        // Name Field -------------------------------------------------------
        Text(
            text = StringResources.NAME_FIELD_TITLE,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .padding(bottom = 12.dp, top = 20.dp)
        )
        GeneralTextField(
            value = userName,
            onValueChange = { userName = it },
            hintText = StringResources.NAME_FIELD_HINT,
            startIcon = Icons.Filled.TextFields
        )

        // Course Field -------------------------------------------------------
        Text(
            text = StringResources.COURSE_FIELD_TITLE,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .padding(bottom = 12.dp, top = 20.dp)
        )
        GeneralTextField(
            value = userCourse,
            onValueChange = { userCourse = it },
            hintText = StringResources.COURSE_FIELD_HINT,
            startIcon = Icons.Outlined.CollectionsBookmark
        )

        // Password Field -------------------------------------------------------
        Text(
            text = StringResources.PASSWORD_FIELD_TITLE,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .padding(bottom = 12.dp, top = 20.dp)
        )
        GeneralTextField(
            value = userPassword,
            onValueChange = { userPassword = it},
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
}

@Composable
private fun RegisterFormButton() {
    Button(
        onClick = {},
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