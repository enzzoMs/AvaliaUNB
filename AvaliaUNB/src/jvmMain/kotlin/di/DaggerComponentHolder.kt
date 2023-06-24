package di

private const val DATABASE_USER = "postgres"
private const val DATABASE_PASSWORD = "postgres"
private const val JDBC_URL = "jdbc:postgresql://localhost:5432/"

object DaggerComponentHolder {
    val appComponent: AppComponent = DaggerAppComponent.builder().databaseModule(
        DatabaseModule(DATABASE_USER, DATABASE_PASSWORD, JDBC_URL)
    ).build()
}