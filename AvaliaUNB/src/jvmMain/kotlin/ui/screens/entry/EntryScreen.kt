package ui.screens.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import di.DaggerComponentHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.screens.login.EntrySideCarouselPanel
import ui.screens.login.LoginFormPanel
import ui.screens.register.RegisterFormPanel
import ui.screens.register.RegisterSuccessfulPanel
import utils.navigation.NavigationComponent
import utils.navigation.NavigationController
import utils.navigation.Screen

@Composable
fun EntryScreen(
    onLoginSuccess: (userRegistrationNumber: String) -> Unit
) {
    val navigationController = remember { NavigationController(startDestination = Screen.LOGIN) }

    Row {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1.2f)
        ) {
            NavigationComponent(
                navigationController = navigationController,
                getDestination = { destination ->
                    when (destination) {
                        Screen.LOGIN -> {
                            val loginViewModel = DaggerComponentHolder.appComponent.getLoginViewModel()

                            LoginFormPanel(
                                loginViewModel = loginViewModel,
                                onRegisterClicked = { navigationController.navigateTo(Screen.REGISTER_FORM) },
                                onLoginButtonClicked = {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        if (loginViewModel.login()) {
                                            onLoginSuccess(loginViewModel.loginUiState.value.registrationNumber)
                                        }
                                    }
                                }
                            )
                        }
                        Screen.REGISTER_FORM -> {
                            val registerViewModel = DaggerComponentHolder.appComponent.getRegisterFormViewModel()

                            RegisterFormPanel(
                                registerFormViewModel = registerViewModel,
                                onBackClicked = { navigationController.navigateBack() },
                                onRegisterButtonClicked = {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        if (registerViewModel.registerUser()) {
                                            navigationController.navigateTo(Screen.REGISTER_SUCCESSFUL)
                                        }
                                    }
                                }
                            )
                        }
                        Screen.REGISTER_SUCCESSFUL -> RegisterSuccessfulPanel(
                            onBackClicked = { navigationController.navigateTo(Screen.LOGIN) }
                        )
                        else -> error("Destination not supported: $destination")
                    }
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(MaterialTheme.colors.primary)
        ) {
            EntrySideCarouselPanel()
        }
    }
}










