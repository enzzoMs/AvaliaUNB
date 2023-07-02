package data.source

import androidx.compose.ui.graphics.toArgb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import utils.navigation.Utils
import java.io.File
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

private const val DEPT_COLOR_TRANSPARENCY_VALUE = 100

private const val SCHEMA_PATH = "src/jvmMain/resources/database/avalia_unb_schema.sql"

private const val DEPARTMENTS_CSV_2022_1_PATH = "src/jvmMain/resources/database/data/2022.1/departamentos_2022-1.csv"
private const val DEPARTMENTS_CSV_2022_2_PATH = "src/jvmMain/resources/database/data/2022.2/departamentos_2022-2.csv"
private const val DEPARTMENTS_CSV_2023_1_PATH = "src/jvmMain/resources/database/data/2023.1/departamentos_2023-1.csv"

private const val SUBJECTS_CSV_2022_1_PATH = "src/jvmMain/resources/database/data/2022.1/disciplinas_2022-1.csv"
private const val SUBJECTS_CSV_2022_2_PATH = "src/jvmMain/resources/database/data/2022.2/disciplinas_2022-2.csv"
private const val SUBJECTS_CSV_2023_1_PATH = "src/jvmMain/resources/database/data/2023.1/disciplinas_2023-1.csv"

private const val SEMESTER_2022_1 = "2022-1"
private const val SEMESTER_2022_2 = "2022-2"
private const val SEMESTER_2023_1 = "2023-1"

@Singleton
class DatabaseManager @Inject constructor(
    @Named("databaseUser") val databaseUser: String,
    @Named("databasePassword") val databasePassword: String,
    @Named("jdbcURL") val jdbcURL: String)
{

    private val databaseConnection = DriverManager.getConnection(jdbcURL, databaseUser, databasePassword)
    private val splitCommasIgnoringQuotes = Regex(",(?=([^\"]*\"[^\"]*\")*[^\"]*\$)")

    init {
        CoroutineScope(Dispatchers.IO).launch {
            initializeSchema()
            initializeDatabaseInformation()
        }
    }

    fun prepareStatement(sqlStatement: String): PreparedStatement = databaseConnection.prepareStatement(sqlStatement)

    fun executeStatement(sqlStatement: String) {
        databaseConnection.prepareStatement(sqlStatement).execute()
    }

    fun executeQuery(sqlQueryStatement: String): ResultSet = databaseConnection.prepareStatement(sqlQueryStatement).executeQuery()

    private fun initializeSchema() {
        val databaseSchema = File(SCHEMA_PATH).readText()

        val statements = databaseSchema.split(";")

        statements.forEach { executeStatement(it) }
    }

    private fun initializeDatabaseInformation() {
        val departments = executeQuery("SELECT * FROM avalia_unb.departamento")
        if (!departments.next()) initializeDepartments()

        val subjects = executeQuery("SELECT * FROM avalia_unb.disciplina")
        if (!subjects.next()) initializeSubjects()
    }

    private fun initializeDepartments() {
        val insertDepartmentStatement = prepareStatement("INSERT INTO avalia_unb.departamento VALUES (?, ?, ?, ?)")

        val databaseCsvFiles = listOf(
            File(DEPARTMENTS_CSV_2022_1_PATH),
            File(DEPARTMENTS_CSV_2022_2_PATH),
            File(DEPARTMENTS_CSV_2023_1_PATH)
        )

        val semesters = listOf(
            SEMESTER_2022_1,
            SEMESTER_2022_2,
            SEMESTER_2023_1
        )

        for ((databaseCSV, semester) in databaseCsvFiles.zip(semesters)) {
            val csvLines = databaseCSV.readLines().drop(1)

            for (line in csvLines) {
                val (code, name) = line.trim().split(splitCommasIgnoringQuotes)

                val randomColor = Utils.getRandomColorWithTransparency(DEPT_COLOR_TRANSPARENCY_VALUE)

                insertDepartmentStatement.setInt(1, code.toInt())
                insertDepartmentStatement.setString(2, name.replace("\"", "").uppercase())
                insertDepartmentStatement.setString(3, semester)
                insertDepartmentStatement.setInt(4, randomColor.toArgb())

                insertDepartmentStatement.execute()

            }
        }
    }

    private fun initializeSubjects() {
        val insertSubjectStatement = prepareStatement(
            "INSERT INTO avalia_unb.disciplina (codigo, nome, semestre, codigo_departamento) VALUES (?, ?, ?, ?)"
        )

        val databaseCsvFiles = listOf(
            File(SUBJECTS_CSV_2022_1_PATH),
            File(SUBJECTS_CSV_2022_2_PATH),
            File(SUBJECTS_CSV_2023_1_PATH)
        )

        val semesters = listOf(
            SEMESTER_2022_1,
            SEMESTER_2022_2,
            SEMESTER_2023_1
        )

        for ((databaseCSV, semester) in databaseCsvFiles.zip(semesters)) {
            val csvLines = databaseCSV.readLines().drop(1)

            for (line in csvLines) {
                val (code, name, code_department) = line.trim().split(splitCommasIgnoringQuotes)

                insertSubjectStatement.setString(1, code)
                insertSubjectStatement.setString(2, name.replace("\"", "").uppercase())
                insertSubjectStatement.setString(3, semester)
                insertSubjectStatement.setInt(4, code_department.toInt())

                insertSubjectStatement.execute()
            }
        }

    }
}



