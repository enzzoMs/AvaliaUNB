package data.source

import androidx.compose.ui.graphics.toComposeImageBitmap
import data.models.TeacherModel
import data.models.TeacherReviewModel
import utils.Utils
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
            val profilePicBytes = if (allTeachersQueryResult.getObject("foto_de_perfil") == null) {
                Utils.getDefaultProfilePictureBytes()
            } else {
                allTeachersQueryResult.getBytes("foto_de_perfil")
            }

            val bufferedProfilePicImage = ImageIO.read(profilePicBytes.inputStream())
            val profilePic = bufferedProfilePicImage.toComposeImageBitmap()

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
                    profilePic,
                )
            )
        }

        return teachers.toList()
    }

    fun getTeacherReviews(teacherName: String, departmentCode: Int): List<TeacherReviewModel> {
        val reviewsQueryResult = database.executeQuery(
            "SELECT avaliacao_professor.*, avaliacao.*, usuario.nome AS usuario_nome, " +
                    "usuario.foto_de_perfil, usuario.matricula " +
                    "FROM avaliacao_professor " +
                    "INNER JOIN avaliacao ON avaliacao_professor.id_avaliacao = avaliacao.id " +
                    "INNER JOIN usuario ON avaliacao.matricula_aluno = usuario.matricula " +
                    "WHERE avaliacao_professor.nome_professor = '$teacherName' AND " +
                    "avaliacao_professor.codigo_departamento = $departmentCode;"
        )

        val teacherReviews = mutableListOf<TeacherReviewModel>()

        while (reviewsQueryResult.next()) {
            val profilePicBytes = if (reviewsQueryResult.getObject("foto_de_perfil") == null) {
                Utils.getDefaultProfilePictureBytes()
            } else {
                reviewsQueryResult.getBytes("foto_de_perfil")
            }

            val bufferedProfilePicImage = ImageIO.read(profilePicBytes.inputStream())
            val profilePic = bufferedProfilePicImage.toComposeImageBitmap()

            teacherReviews.add(
                TeacherReviewModel(
                    reviewsQueryResult.getInt("id"),
                    reviewsQueryResult.getString("comentario") ?: "",
                    reviewsQueryResult.getInt("pontuacao"),
                    profilePic,
                    reviewsQueryResult.getString("usuario_nome"),
                    reviewsQueryResult.getString("matricula"),
                    reviewsQueryResult.getString("nome_professor"),
                    reviewsQueryResult.getInt("codigo_departamento")
                )
            )
        }

        return teacherReviews.toList()
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