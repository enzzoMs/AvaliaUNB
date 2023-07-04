package utils.database

object DatabaseUtils {
    fun getSchemaPath() = DatabasePaths.SCHEMA

    fun getDepartmentCsvPath(semester: Semester) = String.format(
            DatabasePaths.Departments.BASE_CSV_PATH,
            getSemesterFolder(semester),
            getSemesterFileName(semester)
    )

    fun getSubjectCsvPath(semester: Semester) = String.format(
        DatabasePaths.Subjects.BASE_CSV_PATH,
        getSemesterFolder(semester),
        getSemesterFileName(semester)
    )

    fun getClassesAndTeachersCsvPath(semester: Semester) = String.format(
        DatabasePaths.ClassesAndTeachers.BASE_CSV_PATH,
        getSemesterFolder(semester),
        getSemesterFileName(semester)
    )


    private fun getSemesterFolder(semester: Semester) = "${semester.year}.${semester.semester_number}"

    private fun getSemesterFileName(semester: Semester) = "${semester.year}-${semester.semester_number}"

}

private object DatabasePaths {
    const val SCHEMA = "src/jvmMain/resources/database/avalia_unb_schema.sql"
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