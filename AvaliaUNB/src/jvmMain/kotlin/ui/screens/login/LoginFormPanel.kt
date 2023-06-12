package ui.screens.login

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import theme.*
import ui.components.GeneralTextField

@Composable
fun LoginFormPanel() {
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
            LoginRegister()
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
            painter = painterResource("images/logo_unb.svg"),
            contentDescription = null,
            modifier = Modifier
                .size(75.dp, (37.5).dp)
        )
        Text(
            textAlign = TextAlign.Center,
            text = "AvaliaUNB",
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .padding(start = 26.dp)
        )
    }
    Text(
        text = "Veja avaliações de outros estudantes e se informe no período de matrículas!",
        textAlign = TextAlign.Justify,
        style = MaterialTheme.typography.subtitle2,
        modifier = Modifier
            .padding(top = 26.dp, bottom = 24.dp)
    )
}

@Composable
private fun LoginFormFields() {
    Text(
        text = "LOGIN",
        style = MaterialTheme.typography.h6
    )
    Divider(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 14.dp, bottom = 16.dp)
    )
    Text(
        text = "E-MAIL",
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier
            .padding(bottom = 12.dp)
    )
    GeneralTextField(
        value = "",
        onValueChange = {},
        hintText = "Insira seu e-mail",
        startIcon = Icons.Filled.Email
    )
    Text(
        text = "SENHA",
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier
            .padding(bottom = 12.dp, top = 20.dp)
    )
    GeneralTextField(
        value = "",
        onValueChange = {},
        hintText = "Insira sua senha",
        startIcon = Icons.Filled.Lock,
        endIcon = Icons.Filled.Visibility
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
            text = "LOGIN",
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
            text = "Ou",
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
            text = "ENTRAR SEM CONTA",
            style = MaterialTheme.typography.button,
            color = DimGray
        )
    }
}

@Composable
private fun LoginRegister() {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(bottom = 40.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Não possui uma conta?",
            style = MaterialTheme.typography.body2,
            modifier = Modifier
                .padding(end = 6.dp)
        )
        Text(
            text = "Registre-se!",
            style = MaterialTheme.typography.body2,
            color = UnbGreen
        )
    }
}












