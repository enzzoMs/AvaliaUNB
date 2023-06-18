package ui.screens.login

import Screen
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import resources.StringResources
import theme.DimGray
import theme.Platinum
import theme.UnbGreen
import theme.White
import ui.components.GeneralTextField
import utils.navigation.NavigationController
import java.awt.Cursor


@Composable
fun LoginFormPanel(navigationController: NavigationController) {
    Box {
        val stateVertical = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(start = 32.dp, end = 32.dp, top = 32.dp)
                .fillMaxHeight()
                .verticalScroll(stateVertical)
        ) {
            LoginFormTitle()
            LoginFormFields()
            Spacer(
                modifier = Modifier
                    .weight(1f)
            )
            LoginRegisterText(navigationController)
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
            painter = painterResource("images/logo_avalia_unb.svg"),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(90.dp)
        )
        Text(
            textAlign = TextAlign.Center,
            text = StringResources.LOGIN_TITLE,
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .padding(start = 26.dp)
        )
    }
    Text(
        text = StringResources.LOGIN_SUBTITLE,
        textAlign = TextAlign.Justify,
        style = MaterialTheme.typography.subtitle2,
        modifier = Modifier
            .padding(top = 20.dp, bottom = 24.dp)
    )
}

@Composable
private fun LoginFormFields() {
    var userEmail by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }
    var passwordFieldEndIcon by remember { mutableStateOf(Icons.Filled.VisibilityOff) }
    var passwordFieldVisualTransformation: VisualTransformation by remember { mutableStateOf(PasswordVisualTransformation()) }

    Text(
        text = StringResources.LOGIN_FORM_TITLE,
        style = MaterialTheme.typography.h6
    )

    Divider(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 14.dp, bottom = 16.dp)
    )

    // Email Field -------------------------------------------------------
    Text(
        text = StringResources.EMAIL_FIELD_TITLE,
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier
            .padding(bottom = 12.dp)
    )
    GeneralTextField(
        value = userEmail,
        onValueChange = { userEmail = it },
        hintText = StringResources.EMAIL_FIELD_HINT,
        startIcon = Icons.Filled.Email
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

    Button(
        onClick = {},
        shape = RoundedCornerShape(4.dp),
        contentPadding = PaddingValues(vertical = 10.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = UnbGreen,
            contentColor = White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 36.dp)
    ) {
        Text(
            text = StringResources.LOGIN_BUTTON,
            style = MaterialTheme.typography.button
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 22.dp)
                .weight(1f)
        )
        Text(
            text = StringResources.GENERAL_TEXT_OR,
            style = MaterialTheme.typography.subtitle2
        )
        Divider(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .weight(1f)
        )
    }

    Button(
        onClick = {},
        shape = RoundedCornerShape(4.dp),
        contentPadding = PaddingValues(vertical = 10.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Platinum
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = StringResources.LOGIN_WITHOUT_ACCOUNT_BUTTON,
            style = MaterialTheme.typography.button,
            color = DimGray
        )
    }
}

@Composable
private fun LoginRegisterText(navigationController: NavigationController) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(bottom = 40.dp, top = 26.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = StringResources.LOGIN_NO_ACCOUNT_QUESTION,
            style = MaterialTheme.typography.body2,
            modifier = Modifier
                .padding(end = 6.dp)
        )
        Text(
            text = StringResources.LOGIN_NO_ACCOUNT_REGISTER,
            style = MaterialTheme.typography.body2,
            color = UnbGreen,
            modifier = Modifier
                .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)))
                .clickable(
                    onClick = { navigationController.navigateTo(Screen.Register.label) },
                    indication = null,
                    interactionSource = MutableInteractionSource()
                )
        )
    }
}












