package data.source

import data.models.UserModel
import java.io.File
import java.sql.DriverManager
import java.sql.ResultSet

private const val SCHEMA_PATH = "src/jvmMain/kotlin/data/source/avalia_unb_schema.sql"

class AppDatabase(
    databaseUser: String,
    databasePassword: String,
    jdbcURL: String
) {
    private val databaseConnection = DriverManager.getConnection(jdbcURL, databaseUser, databasePassword)

    init {
        val databaseSchema = File(SCHEMA_PATH).readText()

        val statements = databaseSchema.split(";")

        statements.forEach { executeStatement(it) }
    }

    fun executeStatement(sqlStatement: String) = databaseConnection.prepareStatement(sqlStatement).execute()

    fun executeQuery(sqlQueryStatement: String): ResultSet = databaseConnection.prepareStatement(sqlQueryStatement).executeQuery()
}



