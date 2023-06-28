package di
import dagger.Component
import data.models.UserModel
import data.repositories.UserRepository
import ui.screens.login.viewmodel.LoginViewModel
import ui.screens.main.viewmodel.MainScreenViewModel
import ui.screens.register.viewmodel.RegisterFormViewModel
import javax.inject.Singleton

@Singleton @Component(modules = [DatabaseModule::class])
interface AppComponent {

    fun getRegisterFormViewModel(): RegisterFormViewModel

    fun getLoginViewModel(): LoginViewModel

    fun getMainScreenViewModel(): MainScreenViewModel

    fun getUserRepository(): UserRepository
}