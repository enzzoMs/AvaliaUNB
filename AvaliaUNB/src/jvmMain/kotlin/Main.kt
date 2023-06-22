import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import data.source.AppDatabase
import data.source.UserDAO
import theme.AvaliaUnbTheme
import ui.screens.main.MainScreen
import ui.screens.splash.SplashScreen
import utils.navigation.NavigationController
import utils.navigation.NavigationHost

private const val DATABASE_USER = "postgres"
private const val DATABASE_PASSWORD = "postgres"
private const val JDBC_URL = "jdbc:postgresql://localhost:5432/"

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
    val appDatabase = remember { AppDatabase(DATABASE_USER, DATABASE_PASSWORD, JDBC_URL) }

    val navigationController = remember {
        NavigationController(
            startDestination = Screen.Splash.label
        ).apply {
            this.destinations = { destinationLabel ->
                when(destinationLabel) {
                    Screen.Splash.label -> SplashScreen(this)
                    Screen.Login.label -> MainScreen(UserDAO(appDatabase))
                }
            }
        }
    }

    val navigationHost = remember { NavigationHost(navigationController) }

    navigationHost.DisplayContent()
}

