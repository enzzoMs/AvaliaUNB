import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import theme.AvaliaUnbTheme
import ui.screens.login.LoginScreen

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        AvaliaUnbTheme() {
            AppAvaliaUNB()
        }
    }
}

@Composable
@Preview
fun AppAvaliaUNB() {
    LoginScreen()
}

