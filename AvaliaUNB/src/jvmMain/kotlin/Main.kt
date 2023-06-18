import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import theme.AvaliaUnbTheme
import ui.screens.main.MainScreen
import ui.screens.splash.SplashScreen
import utils.navigation.NavigationController
import utils.navigation.NavigationHost

enum class Screen(
    val label: String
) {
    Splash("splash"),
    Login("login"),
    Register("register")
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
                    Screen.Login.label -> MainScreen()
                }
            }
        }
    }

    val navigationHost = remember { NavigationHost(navigationController) }

    navigationHost.DisplayContent()
}

