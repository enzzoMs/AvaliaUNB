package di
import dagger.Component
import dagger.Provides
import data.repositories.ClassRepository
import data.repositories.ReviewRepository
import data.repositories.SubjectRepository
import data.repositories.UserRepository
import data.source.DatabaseManager
import ui.screens.login.viewmodel.LoginViewModel
import ui.screens.register.viewmodel.RegisterFormViewModel
import ui.screens.splash.viewmodel.SplashViewModel
import ui.screens.subjects.all.viewmodel.SubjectsViewModel
import javax.inject.Singleton

@Singleton @Component(modules = [DatabaseModule::class])
interface AppComponent {

    fun getDatabaseManager(): DatabaseManager

    fun getRegisterFormViewModel(): RegisterFormViewModel

    fun getLoginViewModel(): LoginViewModel

    fun getSubjectsViewModel(): SubjectsViewModel

    fun getSplashViewModel(): SplashViewModel

    fun getUserRepository(): UserRepository

    fun getSubjectRepository(): SubjectRepository

    fun getClassRepository(): ClassRepository

    fun getReviewRepository(): ReviewRepository

}