package data.source

import data.models.TeacherModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeacherDAO @Inject constructor(
    private val database: DatabaseManager
) {

    fun getAllTeachers(): List<TeacherModel> {
        val allTeachersQueryResult = database.executeQuery(
            "SELECT * FROM PROFESSORES_INFORMACOES"
        )

        val teachers = mutableListOf<TeacherModel>()

        while (allTeachersQueryResult.next()) {
            val teacherSemesterYear = allTeachersQueryResult.getString("ano_semestre")
            val teacherSemesterNumber = allTeachersQueryResult.getString("numero_semestre")

            val teacherSemester = "${teacherSemesterYear}-${teacherSemesterNumber}"

            teachers.add(
                TeacherModel(
                    allTeachersQueryResult.getString("nome"),
                    allTeachersQueryResult.getString("dept_nome"),
                    allTeachersQueryResult.getInt("codigo_departamento"),
                    teacherSemester,
                    allTeachersQueryResult.getObject("pontuacao") as Double?,
                    allTeachersQueryResult.getInt("num_avaliacoes"),
                    null
                )
            )
        }

        return teachers.toList()
    }

    fun getTeacherScore(teacherName: String, departmentCode: Int): Double? {
        val scoreQueryResult = database.executeQuery(
            "SELECT pontuacao FROM professor " +
                    "WHERE nome = '$teacherName' AND " +
                    "codigo_departamento = $departmentCode"
        )

        scoreQueryResult.next()

        return scoreQueryResult.getObject("pontuacao") as Double?
    }
}