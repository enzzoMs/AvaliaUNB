package ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import utils.resources.ResourcesUtils
import theme.UnbGreen
import theme.White
import ui.components.buttons.PrimaryButton


@Composable
fun RegisterSuccessfulPanel(
    onBackClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
    ) {
        registerSuccessfulIcon(
            modifier = Modifier
                .weight(1f)
        )
        registerSuccessfulMessage(onBackClicked)
    }
}

@Composable
private fun registerSuccessfulIcon(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(UnbGreen)
            .then(modifier)
    ) {
        Icon(
            painter = painterResource(ResourcesUtils.ImagePaths.CHECK_MARK_SPARKLES),
            contentDescription = null,
            tint = White,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp, vertical = 70.dp)
        )
    }
}

@Composable
private fun registerSuccessfulMessage(
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(start = 32.dp, end = 32.dp, top = 40.dp, bottom = 30.dp)
            .then(modifier)
    ) {
        Text(
            text = ResourcesUtils.Strings.REGISTER_SUCCESSFUL_TITLE,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            color = UnbGreen,
            modifier = Modifier
                .padding(bottom = 18.dp)
        )
        Text(
            text = ResourcesUtils.Strings.REGISTER_SUCCESSFUL_MESSAGE,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .padding(bottom = 28.dp)
        )

        Divider(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 64.dp)
        )

        PrimaryButton(
            label = ResourcesUtils.Strings.BACK_TO_LOGIN_BUTTON,
            onClick = onBackClicked,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}