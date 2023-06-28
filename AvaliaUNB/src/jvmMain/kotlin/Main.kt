import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.DaggerComponentHolder
import theme.AvaliaUnbTheme
import ui.screens.entry.EntryScreen
import ui.screens.main.MainScreen
import ui.screens.splash.SplashScreen
import utils.navigation.NavigationController
import utils.navigation.NavigationComponent
import utils.navigation.Screen

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
    val navigationController = remember { NavigationController(startDestination = Screen.SPLASH) }
    var userRegistrationNumber: String? = remember { null }

    NavigationComponent(
        navigationController = navigationController,
        getDestination = { destination ->
            when(destination) {
                Screen.SPLASH -> SplashScreen(
                    onSplashEnd = { navigationController.navigateTo(Screen.ENTRY) }
                )
                Screen.ENTRY -> EntryScreen(
                    onLoginSuccess = { registrationNumber ->
                        userRegistrationNumber = registrationNumber
                        navigationController.navigateTo(Screen.MAIN) }
                )
                Screen.MAIN -> MainScreen(
                    mainScreenViewModel = DaggerComponentHolder.appComponent.getMainScreenViewModel(),
                    onLogout = { navigationController.navigateTo(Screen.ENTRY) },
                    userModel = if (userRegistrationNumber == null) {
                        null
                    } else {
                        DaggerComponentHolder.appComponent.getUserRepository().getUser(userRegistrationNumber!!)
                    }
                )
                else -> error("Destination not supported: $destination")
            }
        }
    )
}

