package data.source

import androidx.compose.ui.graphics.toComposeImageBitmap
import data.models.ClassModel
import data.models.ClassReviewModel
import data.models.TeacherModel
import javax.imageio.ImageIO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassDAO @Inject constructor(
    private val database: DatabaseManager,
) {
    fun getClassTeacher(classModel: ClassModel): TeacherModel {
        val classTeacherQueryResult = database.executeQuery(
            "SELECT * FROM professor " +
                    "WHERE nome = '${classModel.teacherName}' AND codigo_departamento = '${classModel.departmentCode}';"
        )

        classTeacherQueryResult.next()

        val profilePicBytes = classTeacherQueryResult.getBytes("foto_de_perfil")
        val bufferedProfilePicImage = ImageIO.read(profilePicBytes.inputStream())
        val profilePic = bufferedProfilePicImage.toComposeImageBitmap()

        return TeacherModel(
            classTeacherQueryResult.getString("nome"),
            classTeacherQueryResult.getObject("pontuacao") as Float?,
            profilePic
        )
    }

    fun getClassScore(classId: Int): Double? {
        val scoreQueryResult = database.executeQuery(
            "SELECT pontuacao FROM turma " +
                    "WHERE id = $classId"
        )

        scoreQueryResult.next()

        return scoreQueryResult.getObject("pontuacao") as Double?
    }

    fun getClassReviews(classId: Int): List<ClassReviewModel> {
        val reviewsQueryResult = database.executeQuery(
            "SELECT avaliacao_turma.*, avaliacao.*, usuario.nome AS usuario_nome, " +
                    "usuario.foto_de_perfil, usuario.matricula " +
                    "FROM avaliacao_turma " +
                    "INNER JOIN avaliacao ON avaliacao_turma.id_avaliacao = avaliacao.id " +
                    "INNER JOIN usuario ON avaliacao.matricula_aluno = usuario.matricula " +
                    "WHERE avaliacao_turma.id_turma = '$classId';"
        )

        val classReviews = mutableListOf<ClassReviewModel>()

        while (reviewsQueryResult.next()) {
            val profilePicBytes = reviewsQueryResult.getBytes("foto_de_perfil")
            val bufferedProfilePicImage = ImageIO.read(profilePicBytes.inputStream())
            val profilePic = bufferedProfilePicImage.toComposeImageBitmap()

            classReviews.add(
                ClassReviewModel(
                    reviewsQueryResult.getString("comentario"),
                    reviewsQueryResult.getInt("pontuacao"),
                    reviewsQueryResult.getInt("id_turma"),
                    profilePic,
                    reviewsQueryResult.getString("usuario_nome"),
                    reviewsQueryResult.getString("matricula")
                )
            )
        }

        return classReviews.toList()
    }
}