package di

private const val DATABASE_USER = "postgres"
private const val DATABASE_PASSWORD = "postgres"
private const val JDBC_URL = "jdbc:postgresql://localhost:5432/"

object DaggerComponentHolder {
    private var appComponent: AppComponent? = null

    fun getAppComponent(): AppComponent {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder().databaseModule(
                DatabaseModule(DATABASE_USER, DATABASE_PASSWORD, JDBC_URL)
            ).build()
        }

        return appComponent!!
    }
}