package di
import dagger.Component
import data.repositories.UserRepository
import ui.screens.login.viewmodel.LoginViewModel
import ui.screens.register.viewmodel.RegisterFormViewModel
import ui.screens.subjects.viewmodel.SubjectsViewModel
import javax.inject.Singleton

@Singleton @Component(modules = [DatabaseModule::class])
interface AppComponent {

    fun getRegisterFormViewModel(): RegisterFormViewModel

    fun getLoginViewModel(): LoginViewModel

    fun getSubjectsViewModel(): SubjectsViewModel

    fun getUserRepository(): UserRepository
}