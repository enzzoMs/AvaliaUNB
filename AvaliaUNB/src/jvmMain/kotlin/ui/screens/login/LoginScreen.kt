package ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LoginScreen() {
    Row {
        Column(modifier = Modifier
            .fillMaxSize()
            .weight(1f)
        ) {
            LoginFormPanel()
        }
        Column(modifier = Modifier
            .fillMaxSize()
            .weight(1f)
            .background(MaterialTheme.colors.primary)
        ) {}
    }
}










