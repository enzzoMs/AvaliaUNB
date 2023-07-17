package data.source

import androidx.compose.ui.graphics.toComposeImageBitmap
import data.models.TeacherModel
import javax.imageio.ImageIO
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
            val profilePic = if (allTeachersQueryResult.getObject("foto_de_perfil") == null) {
                null
            } else {
                val profilePicBytes = allTeachersQueryResult.getBytes("foto_de_perfil")
                val bufferedProfilePicImage = ImageIO.read(profilePicBytes.inputStream())
                bufferedProfilePicImage.toComposeImageBitmap()
            }

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
                    profilePic
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