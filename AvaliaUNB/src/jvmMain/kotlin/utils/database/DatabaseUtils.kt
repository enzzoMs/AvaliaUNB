package utils.database

import java.io.File
import java.util.Properties

object DatabaseUtils {
    fun getSchemaPath() = DatabasePaths.SCHEMA

    fun getDefaultDataPath() = DatabasePaths.DEFAULT_DATA

    fun getDepartmentCsvPath(prePopulatedSemester: PrePopulatedSemester) = String.format(
            DatabasePaths.Departments.BASE_CSV_PATH,
            getSemesterFolder(prePopulatedSemester),
            getSemesterFileName(prePopulatedSemester)
    )

    fun getSubjectCsvPath(prePopulatedSemester: PrePopulatedSemester) = String.format(
        DatabasePaths.Subjects.BASE_CSV_PATH,
        getSemesterFolder(prePopulatedSemester),
        getSemesterFileName(prePopulatedSemester)
    )

    fun getClassesAndTeachersCsvPath(prePopulatedSemester: PrePopulatedSemester) = String.format(
        DatabasePaths.ClassesAndTeachers.BASE_CSV_PATH,
        getSemesterFolder(prePopulatedSemester),
        getSemesterFileName(prePopulatedSemester)
    )

    fun getDatabaseConfiguration(): DatabaseConfiguration {
        val propertiesFile = File(DatabasePaths.CONFIG_PROPERTIES)

        val databaseProperties = Properties().apply {
            load(propertiesFile.inputStream())
        }

        val reloadDatabase = databaseProperties.getProperty("reiniciar_banco_de_dados").toBoolean()

        databaseProperties.setProperty("reiniciar_banco_de_dados", "false")
        databaseProperties.store(propertiesFile.outputStream(), null)

        return DatabaseConfiguration(
            reloadDatabase = reloadDatabase,
            loadDataForSemester2022_1 = databaseProperties.getProperty("incluir_dados_2022_1").toBoolean(),
            loadDataForSemester2022_2 = databaseProperties.getProperty("incluir_dados_2022_2").toBoolean(),
            loadDataForSemester2023_1 = databaseProperties.getProperty("incluir_dados_2023_1").toBoolean()
        )
    }
    private fun getSemesterFolder(prePopulatedSemester: PrePopulatedSemester) = "${prePopulatedSemester.year}.${prePopulatedSemester.semester_number}"

    private fun getSemesterFileName(prePopulatedSemester: PrePopulatedSemester) = "${prePopulatedSemester.year}-${prePopulatedSemester.semester_number}"

}

private object DatabasePaths {
    const val SCHEMA = "src/jvmMain/resources/database/avalia_unb_schema.sql"
    const val DEFAULT_DATA = "src/jvmMain/resources/database/data/avalia_unb_default_data.sql"
    const val CONFIG_PROPERTIES = "src/jvmMain/resources/database/database_config.properties"
    private const val BASE_DATA_PATH = "src/jvmMain/resources/database/data"

    object Departments {
        const val BASE_CSV_PATH = "$BASE_DATA_PATH/%s/departamentos_%s.csv"
    }

    object Subjects {
        const val BASE_CSV_PATH = "$BASE_DATA_PATH/%s/disciplinas_%s.csv"
    }

    object ClassesAndTeachers {
        const val BASE_CSV_PATH = "$BASE_DATA_PATH/%s/turmas_%s.csv"
    }
}