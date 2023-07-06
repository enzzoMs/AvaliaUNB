import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.DaggerComponentHolder
import theme.AvaliaUnbTheme
import ui.screens.entry.EntryScreen
import ui.screens.main.MainScreen
import ui.screens.main.viewmodel.MainScreenViewModel
import ui.screens.splash.SplashScreen
import utils.navigation.NavigationController
import utils.navigation.NavigationComponent
import utils.navigation.Screen
import javax.swing.UIManager

fun main() = application {
    DaggerComponentHolder.appComponent.getDatabaseManager().configureDatabase()

    try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    } catch (e: Exception) {
        e.printStackTrace()
    }

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
                    splashViewModel = DaggerComponentHolder.appComponent.getSplashViewModel(),
                    onSplashEnd = { navigationController.navigateTo(Screen.ENTRY) }
                )
                Screen.ENTRY -> EntryScreen(
                    onLoginSuccess = { registrationNumber ->
                        userRegistrationNumber = registrationNumber
                        navigationController.navigateTo(Screen.MAIN) }
                )
                Screen.MAIN -> MainScreen(
                    mainScreenViewModel = MainScreenViewModel(
                        userModel =
                            DaggerComponentHolder.appComponent.getUserRepository().getUser(userRegistrationNumber!!)
                    ),
                    onLogout = { navigationController.navigateTo(Screen.ENTRY) },
                    onDeleteAccount = { navigationController.navigateTo(Screen.ENTRY) }
                )
                else -> error("Destination not supported: $destination")
            }
        }
    )
}

