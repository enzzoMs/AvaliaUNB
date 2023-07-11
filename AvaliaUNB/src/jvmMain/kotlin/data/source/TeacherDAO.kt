package data.source

import androidx.compose.ui.graphics.toComposeImageBitmap
import data.models.TeacherReviewModel
import javax.imageio.ImageIO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeacherDAO @Inject constructor(
    private val database: DatabaseManager
) {
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
            val profilePicBytes = reviewsQueryResult.getBytes("foto_de_perfil")
            val bufferedProfilePicImage = ImageIO.read(profilePicBytes.inputStream())
            val profilePic = bufferedProfilePicImage.toComposeImageBitmap()

            teacherReviews.add(
                TeacherReviewModel(
                    reviewsQueryResult.getInt("id"),
                    reviewsQueryResult.getString("comentario"),
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