package di
import dagger.Component
import ui.screens.login.viewmodel.LoginViewModel
import ui.screens.register.viewmodel.RegisterFormViewModel
import javax.inject.Singleton

@Singleton @Component(modules = [DatabaseModule::class])
interface AppComponent {

    fun getRegisterFormViewModel(): RegisterFormViewModel

    fun getLoginViewModel(): LoginViewModel
}