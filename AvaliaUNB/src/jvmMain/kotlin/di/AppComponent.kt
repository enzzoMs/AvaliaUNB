package di
import dagger.Component
import dagger.Provides
import data.repositories.UserRepository
import data.source.UserDAO
import ui.screens.register.viewmodel.RegisterViewModel
import javax.inject.Named
import javax.inject.Singleton

@Singleton @Component(modules = [DatabaseModule::class])
interface AppComponent {

    @Singleton
    fun getRegisterViewModel() : RegisterViewModel
}