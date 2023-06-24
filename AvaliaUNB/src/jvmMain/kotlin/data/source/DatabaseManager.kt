package data.source

import kotlinx.coroutines.*
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

private const val SCHEMA_PATH = "src/jvmMain/kotlin/data/source/avalia_unb_schema.sql"

@Singleton
class DatabaseManager @Inject constructor(
    @Named("databaseUser") val databaseUser: String,
    @Named("databasePassword") val databasePassword: String,
    @Named("jdbcURL") val jdbcURL: String)
{

    private val databaseConnection = DriverManager.getConnection(jdbcURL, databaseUser, databasePassword)

    init {
        val databaseSchema = File(SCHEMA_PATH).readText()

        val statements = databaseSchema.split(";")

        statements.forEach { executeStatement(it) }
    }

    fun executeStatement(sqlStatement: String) {
        databaseConnection.prepareStatement(sqlStatement).execute()
    }

    fun executeQuery(sqlQueryStatement: String): ResultSet = databaseConnection.prepareStatement(sqlQueryStatement).executeQuery()
}



