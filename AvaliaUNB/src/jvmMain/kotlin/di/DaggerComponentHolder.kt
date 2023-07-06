package di

private const val DATABASE_URL = "jdbc:sqlite:src/jvmMain/resources/database/avalia_unb_database.db"

object DaggerComponentHolder {
    val appComponent: AppComponent = DaggerAppComponent.builder().databaseModule(
        DatabaseModule(DATABASE_URL)
    ).build()
}