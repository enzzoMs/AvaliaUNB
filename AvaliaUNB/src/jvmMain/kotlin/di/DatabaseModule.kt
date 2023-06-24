package di

import dagger.Provides
import dagger.Module
import javax.inject.Named

@Module
class DatabaseModule(
    private val databaseUser: String,
    private val databasePassword: String,
    private val jdbcURL: String
) {

    @Provides @Named("databaseUser")
    fun provideDatabaseUser() = databaseUser

    @Provides @Named("databasePassword")
    fun provideDatabasePassword() = databasePassword

    @Provides @Named("jdbcURL")
    fun provideJdbcURL() = jdbcURL

}
