import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import theme.AvaliaUnbTheme
import theme.White
import ui.screens.login.LoginScreen
import ui.screens.splash.SplashScreen
import utils.navigation.NavigationController
import utils.navigation.NavigationHost

enum class Screen(
    val label: String
) {
    Splash("splash"),
    Login("login")
}


fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        AvaliaUnbTheme {
            AppAvaliaUNB()
        }
    }
}

@Composable
@Preview
fun AppAvaliaUNB() {
    val navigationController = remember {
        NavigationController(
            startDestination = Screen.Splash.label
        ).apply {
            this.destinations = { destinationLabel ->
                when(destinationLabel) {
                    Screen.Splash.label -> SplashScreen(this)
                    Screen.Login.label -> LoginScreen()
                }
            }
        }
    }

    val navigationHost = remember {
        NavigationHost(
            navigationController = navigationController,
            modifier = Modifier
                .background(White)
        )
    }

    navigationHost.DisplayContent()
}

