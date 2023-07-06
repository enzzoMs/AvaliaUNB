package data.source

import androidx.compose.ui.graphics.toArgb
import data.source.loading.DatabaseLoadingStatus
import data.source.loading.LoadingStatus
import data.source.loading.SemesterLoadingStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import utils.Utils
import utils.database.DatabaseConfiguration
import utils.database.DatabaseUtils
import utils.database.PrePopulatedSemester
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
    @Named("databaseURL") val databaseURL: String,
    @Named("databaseConfiguration") val databaseConfiguration: DatabaseConfiguration
) {

    private val databaseConnection = DriverManager.getConnection(databaseURL)
    private val splitCommasIgnoringQuotes = Regex(",(?=([^\"]*\"[^\"]*\")*[^\"]*\$)")

    private var _databaseLoadingStatus = MutableStateFlow(
        DatabaseLoadingStatus(
        semestersLoadingStatus = buildList {
            if (databaseConfiguration.loadDataForSemester2022_1) add(SemesterLoadingStatus(PrePopulatedSemester.PRE_POPULATED_SEMESTER_2022_1))
            if (databaseConfiguration.loadDataForSemester2022_2) add(SemesterLoadingStatus(PrePopulatedSemester.PrePopulatedSEMESTER_2022_2))
            if (databaseConfiguration.loadDataForSemester2023_1) add(SemesterLoadingStatus(PrePopulatedSemester.PrePopulatedSEMESTER_2023_1))
        })
    )
    val databaseLoadingStatus = _databaseLoadingStatus.asStateFlow()


    fun configureDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            if (databaseConfiguration.initializeDatabase) {
                initializeDatabase()
            } else {
                _databaseLoadingStatus.update { databaseLoadingStatus ->
                    databaseLoadingStatus.copy(
                        finishedLoading = true
                    )
                }
            }
        }
    }

    fun prepareStatement(sqlStatement: String): PreparedStatement = databaseConnection.prepareStatement(sqlStatement)

    fun executeStatement(sqlStatement: String) = databaseConnection.prepareStatement(sqlStatement).execute()

    fun executeQuery(sqlQueryStatement: String): ResultSet = databaseConnection.prepareStatement(sqlQueryStatement).executeQuery()

    private fun initializeDatabase() {
        deleteAllTableData()
        initializeDatabaseSchema()
        initializeDatabaseData()
    }

    private fun deleteAllTableData() {
        val tablesResultSet = databaseConnection.metaData.getTables(null, null, null, arrayOf("TABLE"))

        while (tablesResultSet.next()) {
            val tableName = tablesResultSet.getString("TABLE_NAME")
            executeStatement("DELETE FROM $tableName")
        }
    }

    private fun initializeDatabaseSchema() {
        _databaseLoadingStatus.update { databaseLoadingStatus ->
            databaseLoadingStatus.copy(
                schemaStatus = LoadingStatus.LOADING
            )
        }

        val databaseSchema = File(DatabaseUtils.getSchemaPath()).readText()

        val statements = databaseSchema.split(";")

        statements.forEach { executeStatement(it)  }

        _databaseLoadingStatus.update { databaseLoadingStatus ->
            databaseLoadingStatus.copy(
                schemaStatus = LoadingStatus.COMPLETED
            )
        }
    }

    private fun initializeDatabaseData() {
        databaseConnection.autoCommit = false

        val semesters = _databaseLoadingStatus.value.semestersLoadingStatus.map { it.prePopulatedSemester }

        for (semester in semesters) {
            initializeSemester(semester)

            // Departments ------------------------------------------------------
            updateSemesterLoadingStatusForItem(
                prePopulatedSemester = semester,
                updateItem = { it.copy(departmentsStatus = LoadingStatus.LOADING)}
            )

            initializeDepartments(semester)

            updateSemesterLoadingStatusForItem(
                prePopulatedSemester = semester,
                updateItem = { it.copy(departmentsStatus = LoadingStatus.COMPLETED)}
            )

            // Subjects ------------------------------------------------------
            updateSemesterLoadingStatusForItem(
                prePopulatedSemester = semester,
                updateItem = { it.copy(subjectsStatus = LoadingStatus.LOADING)}
            )

            initializeSubjects(semester)

            updateSemesterLoadingStatusForItem(
                prePopulatedSemester = semester,
                updateItem = { it.copy(subjectsStatus = LoadingStatus.COMPLETED)}
            )

            // Classes ------------------------------------------------------
            updateSemesterLoadingStatusForItem(
                prePopulatedSemester = semester,
                updateItem = { it.copy(classesStatus = LoadingStatus.LOADING)}
            )

            initializeClassesAndTeachers(semester)

            updateSemesterLoadingStatusForItem(
                prePopulatedSemester = semester,
                updateItem = { it.copy(classesStatus = LoadingStatus.COMPLETED)}
            )
        }

        _databaseLoadingStatus.update { databaseLoadingStatus ->
            databaseLoadingStatus.copy(
                finishedLoading = true
            )
        }

        databaseConnection.autoCommit = true
    }

    private fun updateSemesterLoadingStatusForItem(
        prePopulatedSemester: PrePopulatedSemester,
        updateItem: (SemesterLoadingStatus) -> SemesterLoadingStatus
    ) {
        val updatedSemestersLoadingStatus = _databaseLoadingStatus.value.semestersLoadingStatus.map {
            if (it.prePopulatedSemester == prePopulatedSemester) {
                updateItem(it)
            } else {
                it
            }
        }
        _databaseLoadingStatus.update { databaseLoadingStatus ->
            databaseLoadingStatus.copy(
                semestersLoadingStatus = updatedSemestersLoadingStatus
            )
        }
    }

    private fun initializeSemester(prePopulatedSemester: PrePopulatedSemester) = executeStatement(
        "INSERT INTO semestre (ano, numero_semestre, data_inicio, data_fim) " +
        "VALUES (${prePopulatedSemester.year}, ${prePopulatedSemester.semester_number}, ${prePopulatedSemester.begin_date}, ${prePopulatedSemester.end_date})"
    )


    private fun initializeDepartments(prePopulatedSemester: PrePopulatedSemester) {
        val insertDepartmentStatement = prepareStatement(
            "INSERT INTO departamento (codigo, nome, ano_semestre, numero_semestre, cor) " +
                    "VALUES (?, ?, ?, ?, ?)"
        )

        val databaseCsvFile = File(DatabaseUtils.getDepartmentCsvPath(prePopulatedSemester))
        val csvLines = databaseCsvFile.readLines().drop(1)

        for (line in csvLines) {
            val (code, name) = line.trim().split(splitCommasIgnoringQuotes)

            val randomColor = Utils.getRandomColorWithTransparency(DEPT_COLOR_TRANSPARENCY_VALUE)

            insertDepartmentStatement.setInt(1, code.toInt())
            insertDepartmentStatement.setString(2, name.replace("\"", "").uppercase())
            insertDepartmentStatement.setInt(3, prePopulatedSemester.year)
            insertDepartmentStatement.setInt(4, prePopulatedSemester.semester_number)
            insertDepartmentStatement.setInt(5, randomColor.toArgb())

            insertDepartmentStatement.execute()
        }
    }


    private fun initializeSubjects(prePopulatedSemester: PrePopulatedSemester) {

        val insertSubjectStatement = prepareStatement(
            "INSERT INTO disciplina (codigo, nome, ano_semestre, numero_semestre, codigo_departamento) " +
                    "VALUES (?, ?, ?, ?, ?)"
        )

        val databaseCsvFile = File(DatabaseUtils.getSubjectCsvPath(prePopulatedSemester))
        val csvLines = databaseCsvFile.readLines().drop(1)

        for (line in csvLines) {
            val (code, name, code_department) = line.trim().split(splitCommasIgnoringQuotes)

            insertSubjectStatement.setString(1, code)
            insertSubjectStatement.setString(2, name.replace("\"", "").uppercase())
            insertSubjectStatement.setInt(3, prePopulatedSemester.year)
            insertSubjectStatement.setInt(4, prePopulatedSemester.semester_number)
            insertSubjectStatement.setInt(5, code_department.toInt())

            insertSubjectStatement.execute()
        }
    }

    private fun initializeClassesAndTeachers(prePopulatedSemester: PrePopulatedSemester) {
        val insertClassStatement = prepareStatement(
            "INSERT INTO turma " +
                    "(codigo_turma, horario, num_horas, vagas_total, vagas_ocupadas, local_aula, nome_professor,  " +
                    "id_disciplina, codigo_departamento) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
        )

        val insertTeacherStatement = prepareStatement(
            "INSERT INTO professor VALUES (?, ?, ?, ?) " +
                    "ON CONFLICT (nome, codigo_departamento) DO NOTHING "
        )

        val regexNumberBetweenParentheses = Regex("(\\d+)")
        val regexExcludeNumberParentheses = Regex("[^()\\dh]+")
        val regexTextBetweenParentheses = Regex("\\([^()]*\\)")

        val databaseCsvFile = File(DatabaseUtils.getClassesAndTeachersCsvPath(prePopulatedSemester))
        val csvLines = databaseCsvFile.readLines().drop(1)

        for (line in csvLines) {
            val lineSplit = line.trim().split(splitCommasIgnoringQuotes)

            val classCode = lineSplit[0]
            val teacherName = regexExcludeNumberParentheses.find(lineSplit[2])?.value?.trim() ?: ""
            val numOfHours = regexNumberBetweenParentheses.find(lineSplit[2])?.value?.toInt() ?: 0

            var classSchedule = lineSplit[3].replace(regexTextBetweenParentheses, "").trim()
            classSchedule = classSchedule.ifEmpty { DEFAULT_CLASS_SCHEDULE }

            var filledSeats = lineSplit[4].toInt()

            var totalSeats = when {
                (lineSplit[5].isEmpty() && filledSeats == 0) -> DEFAULT_NUMBER_OF_CLASS_SEATS
                lineSplit[5].isEmpty() -> filledSeats
                else -> lineSplit[5].toInt()
            }

            if (totalSeats < filledSeats) {
                val temp = totalSeats
                totalSeats = filledSeats
                filledSeats = temp
            }


            val location = lineSplit[6].ifEmpty { DEFAULT_CLASS_LOCATION }
            val subjectCode = lineSplit[7]
            val departmentCode = lineSplit[8].toInt()

            var subjectId = findFreeSubjectId(subjectCode, departmentCode, prePopulatedSemester)

            if (subjectId == null) {
                subjectId = getAnySubjectId(subjectCode, departmentCode, prePopulatedSemester) ?: error(
                    "No subject (code ${subjectCode}) for class of teacher $teacherName on semester $prePopulatedSemester"
                )
            }

            insertTeacherStatement.setString(1, teacherName)
            insertTeacherStatement.setInt(2, departmentCode)
            insertTeacherStatement.setInt(3, prePopulatedSemester.year)
            insertTeacherStatement.setInt(4, prePopulatedSemester.semester_number)

            insertTeacherStatement.execute()

            insertClassStatement.setString(1, classCode)
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

    private fun findFreeSubjectId(subjectCode: String, departmentCode: Int, prePopulatedSemester: PrePopulatedSemester): Int? {
        val freeSubjectIds = executeQuery(
            "SELECT id " +
                    "FROM disciplina AS disc " +
                    "WHERE NOT EXISTS (" +
                    "SELECT 1 " +
                    "FROM turma " +
                    "WHERE turma.id_disciplina = disc.id) " +
                    "AND disc.codigo = '$subjectCode' AND disc.codigo_departamento = '$departmentCode' " +
                    "AND disc.ano_semestre = '${prePopulatedSemester.year}' AND disc.numero_semestre = ${prePopulatedSemester.semester_number};"
        )

        return if (freeSubjectIds.next()) freeSubjectIds.getInt("id") else null
    }

    private fun getAnySubjectId(subjectCode: String, departmentCode: Int, prePopulatedSemester: PrePopulatedSemester): Int? {
        val freeSubjectIds = executeQuery(
            "SELECT disc.id " +
                    "FROM disciplina as disc " +
                    "WHERE disc.codigo = '$subjectCode' AND disc.codigo_departamento = '$departmentCode'" +
                    "AND disc.ano_semestre = '${prePopulatedSemester.year}' AND disc.numero_semestre = ${prePopulatedSemester.semester_number};"
        )

        return if (freeSubjectIds.next()) freeSubjectIds.getInt("id") else null
    }
}



