package ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import data.repositories.UserRepository
import data.source.AppDatabase
import data.source.UserDAO
import ui.screens.login.LoginFormPanel
import ui.screens.login.MainSideCarouselPanel
import ui.screens.register.RegisterFormPanel
import ui.screens.register.viewmodel.RegisterViewModel
import utils.navigation.NavigationController
import utils.navigation.NavigationHost

@Composable
fun MainScreen(userDao: UserDAO) {
    val navigationController = remember {
        NavigationController(
            startDestination = Screen.Login.label
        ).apply {
            this.destinations = { destinationLabel ->
                when(destinationLabel) {
                    Screen.Login.label -> LoginFormPanel(this)
                    Screen.Register.label -> RegisterFormPanel(
                        RegisterViewModel(
                            navigationController = this,
                            userRepository = UserRepository(userDao)
                        )
                    )
                }
            }
        }
    }

    val navigationHost = remember { NavigationHost(navigationController) }

    Row {
        Column(modifier = Modifier
            .fillMaxSize()
            .weight(1.2f)
        ) {
            navigationHost.DisplayContent()
        }
        Column(modifier = Modifier
            .fillMaxSize()
            .weight(1f)
            .background(MaterialTheme.colors.primary)
        ) {
            MainSideCarouselPanel()
        }
    }
}










