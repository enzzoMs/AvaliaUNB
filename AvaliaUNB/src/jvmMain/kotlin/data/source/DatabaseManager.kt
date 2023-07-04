package data.source

import androidx.compose.ui.graphics.toArgb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import utils.Utils
import utils.database.DatabaseUtils
import utils.database.Semester
import java.io.File
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

private const val DEPT_COLOR_TRANSPARENCY_VALUE = 100
private const val DEFAULT_NUMBER_OF_CLASS_SEATS = 50
private const val DEFAULT_CLASS_LOCATION = "Local a definir."
private const val DEFAULT_CLASS_SCHEDULE = "Horário a definir."

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
        val databaseSchema = File(DatabaseUtils.getSchemaPath()).readText()

        val statements = databaseSchema.split(";")

        statements.forEach { executeStatement(it) }
    }


    private fun initializeDatabaseInformation() {
        val semesters = executeQuery("SELECT * FROM avalia_unb.semestre")
        if (!semesters.next()) initializeSemesters()

        val departments = executeQuery("SELECT * FROM avalia_unb.departamento")
        if (!departments.next()) initializeDepartments()

        val subjects = executeQuery("SELECT * FROM avalia_unb.disciplina")
        if (!subjects.next()) initializeSubjects()

        val classes = executeQuery("SELECT * FROM avalia_unb.turma")
        if (!classes.next()) initializeClassesAndTeachers()
    }

    private fun initializeSemesters() {
        val insertSemesterStatement = prepareStatement(
            "INSERT INTO avalia_unb.semestre (ano, numero_semestre, data_inicio, data_fim) " +
                    "VALUES (?, ?, ?, ?)"
        )

        for (semester in Semester.values()) {
            insertSemesterStatement.setInt(1, semester.year)
            insertSemesterStatement.setInt(2, semester.semester_number)
            insertSemesterStatement.setDate(3, semester.begin_date)
            insertSemesterStatement.setDate(4, semester.end_date)
            insertSemesterStatement.execute()
        }
    }


    private fun initializeDepartments() {
        val insertDepartmentStatement = prepareStatement(
            "INSERT INTO avalia_unb.departamento (codigo, nome, ano_semestre, numero_semestre, cor) " +
                    "VALUES (?, ?, ?, ?, ?)"
        )

        for (semester in Semester.values()) {
            val databaseCsvFile = File(DatabaseUtils.getDepartmentCsvPath(semester))
            val csvLines = databaseCsvFile.readLines().drop(1)

            for (line in csvLines) {
                val (code, name) = line.trim().split(splitCommasIgnoringQuotes)

                val randomColor = Utils.getRandomColorWithTransparency(DEPT_COLOR_TRANSPARENCY_VALUE)

                insertDepartmentStatement.setInt(1, code.toInt())
                insertDepartmentStatement.setString(2, name.replace("\"", "").uppercase())
                insertDepartmentStatement.setInt(3, semester.year)
                insertDepartmentStatement.setInt(4, semester.semester_number)
                insertDepartmentStatement.setInt(5, randomColor.toArgb())

                insertDepartmentStatement.execute()

            }
        }
    }

    private fun initializeSubjects() {
        val insertSubjectStatement = prepareStatement(
            "INSERT INTO avalia_unb.disciplina (codigo, nome, ano_semestre, numero_semestre, codigo_departamento) " +
                    "VALUES (?, ?, ?, ?, ?)"
        )

        for (semester in Semester.values()) {
            val databaseCsvFile = File(DatabaseUtils.getSubjectCsvPath(semester))
            val csvLines = databaseCsvFile.readLines().drop(1)

            for (line in csvLines) {
                val (code, name, code_department) = line.trim().split(splitCommasIgnoringQuotes)

                insertSubjectStatement.setString(1, code)
                insertSubjectStatement.setString(2, name.replace("\"", "").uppercase())
                insertSubjectStatement.setInt(3, semester.year)
                insertSubjectStatement.setInt(4, semester.semester_number)
                insertSubjectStatement.setInt(5, code_department.toInt())

                insertSubjectStatement.execute()
            }
        }
    }

    private fun initializeClassesAndTeachers() {
        val insertClassStatement = prepareStatement(
            "INSERT INTO avalia_unb.turma " +
                    "(numero, horario, num_horas, vagas_total, vagas_ocupadas, local_aula, nome_professor,  " +
                    "id_disciplina, codigo_departamento) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
        )

        val insertTeacherStatement = prepareStatement(
            "INSERT INTO avalia_unb.professor VALUES (?, ?, ?, ?) " +
                    "ON CONFLICT (nome, codigo_departamento) DO NOTHING "
        )

        val regexNumberBetweenParentheses = Regex("(\\d+)")
        val regexExcludeNumberParentheses = Regex("[^()\\dh]+")
        val regexTextBetweenParentheses = Regex("\\([^()]*\\)")

        for (semester in Semester.values()) {
            val databaseCsvFile = File(DatabaseUtils.getClassesAndTeachersCsvPath(semester))
            val csvLines = databaseCsvFile.readLines().drop(1)

            for (line in csvLines) {
                val lineSplit = line.trim().split(splitCommasIgnoringQuotes)

                val classNumber = lineSplit[0].toInt()
                val teacherName = regexExcludeNumberParentheses.find(lineSplit[2])?.value?.trim() ?: ""
                val numOfHours = regexNumberBetweenParentheses.find(lineSplit[2])?.value?.toInt() ?: 0

                var classSchedule = lineSplit[3].replace(regexTextBetweenParentheses, "").trim()
                classSchedule = classSchedule.ifEmpty { DEFAULT_CLASS_SCHEDULE }

                val filledSeats = lineSplit[4].toInt()

                val totalSeats = if (lineSplit[5].isEmpty() && filledSeats == 0) {
                    DEFAULT_NUMBER_OF_CLASS_SEATS
                } else if (lineSplit[5].isEmpty()) {
                    filledSeats
                } else {
                    lineSplit[5].toInt()
                }

                val location = lineSplit[6].ifEmpty { DEFAULT_CLASS_LOCATION }
                val subjectCode = lineSplit[7]
                val departmentCode = lineSplit[8].toInt()

                var subjectId = findFreeSubjectId(subjectCode, departmentCode, semester)

                if (subjectId == null) {
                    subjectId = getAnySubjectId(subjectCode, departmentCode, semester) ?: error(
                        "No subject (code ${subjectCode}) for class of teacher $teacherName on semester $semester"
                    )
                }

                insertTeacherStatement.setString(1, teacherName)
                insertTeacherStatement.setInt(2, departmentCode)
                insertTeacherStatement.setInt(3, semester.year)
                insertTeacherStatement.setInt(4, semester.semester_number)

                insertTeacherStatement.execute()

                insertClassStatement.setInt(1, classNumber)
                insertClassStatement.setString(2, classSchedule)
                insertClassStatement.setInt(3, numOfHours)
                insertClassStatement.setInt(4, totalSeats)
                insertClassStatement.setInt(5, filledSeats)
                insertClassStatement.setString(6, location)
                insertClassStatement.setString(7, teacherName)
                insertClassStatement.setInt(8, subjectId)
                insertClassStatement.setInt(9, departmentCode)

                insertClassStatement.execute()
            }
        }
    }

    private fun findFreeSubjectId(subjectCode: String, departmentCode: Int, semester: Semester): Int? {
        val freeSubjectIds = executeQuery(
            "SELECT disc.id " +
                    "FROM avalia_unb.disciplina as disc LEFT JOIN avalia_unb.turma as turma " +
                    "ON disc.id = turma.id_disciplina " +
                    "WHERE disc.codigo = '$subjectCode' AND disc.codigo_departamento = '$departmentCode'" +
                    "AND disc.ano_semestre = '${semester.year}' AND disc.numero_semestre = ${semester.semester_number} " +
                    "AND turma.id_disciplina IS NULL"
        )

        return if (freeSubjectIds.next()) freeSubjectIds.getInt("id") else null
    }

    private fun getAnySubjectId(subjectCode: String, departmentCode: Int, semester: Semester): Int? {
        val freeSubjectIds = executeQuery(
            "SELECT disc.id " +
                    "FROM avalia_unb.disciplina as disc " +
                    "WHERE disc.codigo = '$subjectCode' AND disc.codigo_departamento = '$departmentCode'" +
                    "AND disc.ano_semestre = '${semester.year}' AND disc.numero_semestre = ${semester.semester_number};"
        )

        return if (freeSubjectIds.next()) freeSubjectIds.getInt("id") else null
    }
}



