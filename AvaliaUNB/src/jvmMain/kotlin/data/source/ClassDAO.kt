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
    private val teacherDAO: TeacherDAO
) {
    fun getClassTeacher(classModel: ClassModel): TeacherModel {
        val classTeacherQueryResult = database.executeQuery(
            "SELECT professor.*, departamento.nome AS dept_nome " +
                    "FROM professor " +
                    "INNER JOIN departamento " +
                    "ON professor.codigo_departamento = departamento.codigo AND " +
                    "professor.ano_semestre = departamento.ano_semestre AND " +
                    "professor.numero_semestre = departamento.numero_semestre " +
                    "WHERE professor.nome = '${classModel.teacherName}' AND " +
                    "professor.codigo_departamento = '${classModel.departmentCode}';"
        )

        classTeacherQueryResult.next()

        val profilePicBytes = classTeacherQueryResult.getBytes("foto_de_perfil")
        val bufferedProfilePicImage = ImageIO.read(profilePicBytes.inputStream())
        val profilePic = bufferedProfilePicImage.toComposeImageBitmap()

        val teacherSemesterYear = classTeacherQueryResult.getString("ano_semestre")
        val teacherSemesterNumber = classTeacherQueryResult.getString("numero_semestre")

        val teacherSemester = "${teacherSemesterYear}-${teacherSemesterNumber}"

        return TeacherModel(
            classTeacherQueryResult.getString("nome"),
            classTeacherQueryResult.getString("dept_nome"),
            classTeacherQueryResult.getInt("codigo_departamento"),
            teacherSemester,
            classTeacherQueryResult.getObject("pontuacao") as Double?,
            teacherDAO.getTeacherReviews(
                classTeacherQueryResult.getString("nome"),
                classTeacherQueryResult.getInt("codigo_departamento")
            ).size,
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
                    reviewsQueryResult.getInt("id"),
                    reviewsQueryResult.getString("comentario"),
                    reviewsQueryResult.getInt("pontuacao"),
                    profilePic,
                    reviewsQueryResult.getString("usuario_nome"),
                    reviewsQueryResult.getString("matricula"),
                    reviewsQueryResult.getInt("id_turma"),
                )
            )
        }

        return classReviews.toList()
    }
}