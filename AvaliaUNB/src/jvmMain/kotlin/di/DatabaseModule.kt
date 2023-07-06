package di

import dagger.Provides
import dagger.Module
import data.source.DatabaseManager
import utils.database.DatabaseUtils
import javax.inject.Named
import javax.inject.Singleton

@Module
class DatabaseModule(
    private val databaseURL: String,
) {

    @Provides @Named("databaseURL")
    fun provideDatabaseURL() = databaseURL

    @Provides @Singleton @Named("databaseConfiguration")
    fun provideDatabaseConfiguration() = DatabaseUtils.getDatabaseConfiguration()

    @Provides @Named("databaseLoadingStatus")
    fun provideDatabaseLoadingStatus(databaseManager: DatabaseManager) = databaseManager.databaseLoadingStatus
}
