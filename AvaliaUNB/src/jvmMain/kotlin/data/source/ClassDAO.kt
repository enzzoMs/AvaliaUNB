package data.source

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toComposeImageBitmap
import data.models.*
import utils.Utils
import javax.imageio.ImageIO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassDAO @Inject constructor(
    private val database: DatabaseManager,
    private val teacherDAO: TeacherDAO
) {

    fun getAllClasses(): List<ClassModel> {
        val allClassesQuery = database.executeQuery(
            "SELECT * FROM TURMAS_INFORMACOES"
        )

        val classes = mutableListOf<ClassModel>()

        while (allClassesQuery.next()) {
            classes.add(
                ClassModel(
                    allClassesQuery.getInt("id"),
                    allClassesQuery.getString("disc_nome"),
                    allClassesQuery.getString("disc_cod"),
                    allClassesQuery.getString("dept_nome"),
                    allClassesQuery.getString("codigo_departamento"),
                    allClassesQuery.getString("codigo_turma"),
                    allClassesQuery.getString("horario"),
                    allClassesQuery.getInt("num_horas"),
                    allClassesQuery.getInt("vagas_ocupadas"),
                    allClassesQuery.getInt("vagas_total"),
                    allClassesQuery.getString("local_aula"),
                    allClassesQuery.getString("nome_professor"),
                    SemesterModel(
                        allClassesQuery.getInt("ano"),
                        allClassesQuery.getInt("numero_semestre"),
                        allClassesQuery.getString("data_inicio"),
                        allClassesQuery.getString("data_fim"),
                    ),
                    Color(allClassesQuery.getInt("dept_cor")),
                    allClassesQuery.getObject("pontuacao") as Double?,
                    getClassReviews(allClassesQuery.getInt("id")).size
                )
            )
        }

        return classes.toList()
    }

    fun getClassTeacher(classModel: ClassModel): TeacherModel {
        val classTeacherQueryResult = database.executeQuery(
            "SELECT * FROM PROFESSORES_INFORMACOES " +
                    "WHERE nome = '${classModel.teacherName}' AND " +
                    "codigo_departamento = '${classModel.departmentCode}';"
        )

        classTeacherQueryResult.next()

        val profilePicBytes = if (classTeacherQueryResult.getObject("foto_de_perfil") == null) {
            Utils.getDefaultProfilePictureBytes()
        } else {
            classTeacherQueryResult.getBytes("foto_de_perfil")
        }

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
            classTeacherQueryResult.getInt("num_avaliacoes"),
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
            val profilePicBytes = if (reviewsQueryResult.getObject("foto_de_perfil") == null) {
                Utils.getDefaultProfilePictureBytes()
            } else {
                reviewsQueryResult.getBytes("foto_de_perfil")
            }

            val bufferedProfilePicImage = ImageIO.read(profilePicBytes.inputStream())
            val profilePic = bufferedProfilePicImage.toComposeImageBitmap()

            classReviews.add(
                ClassReviewModel(
                    reviewsQueryResult.getInt("id"),
                    reviewsQueryResult.getString("comentario") ?: "",
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