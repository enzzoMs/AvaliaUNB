package data.source

import androidx.compose.ui.graphics.toComposeImageBitmap
import data.models.ClassReviewModel
import data.models.ReviewModel
import data.models.TeacherReviewModel
import javax.imageio.ImageIO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewDAO @Inject constructor(
    private val database: DatabaseManager,
    private val reportDAO: ReportDAO
) {
    fun insertClassReview(reviewModel: ClassReviewModel) {
        val reviewInsertStatement = "INSERT INTO avaliacao (id, comentario, pontuacao, matricula_aluno) " +
                "VALUES (?, ?, ?, ?)"

        val reviewPreparedStatement = database.prepareStatement(reviewInsertStatement)

        reviewModel.apply {
            reviewPreparedStatement.setInt(1, reviewModel.id)
            reviewPreparedStatement.setString(2, comment)
            reviewPreparedStatement.setInt(3, rating)
            reviewPreparedStatement.setString(4, userRegistrationNumber)
        }

        reviewPreparedStatement.execute()

        val classReviewInsertStatement = "INSERT INTO avaliacao_turma (id_avaliacao, id_turma) " +
                "VALUES (?, ?)"

        val classReviewPreparedStatement = database.prepareStatement(classReviewInsertStatement)

        reviewModel.apply {
            classReviewPreparedStatement.setInt(1, reviewModel.id)
            classReviewPreparedStatement.setInt(2, classId)
        }

        classReviewPreparedStatement.execute()
    }

    fun insertTeacherReview(reviewModel: TeacherReviewModel) {
        val reviewInsertStatement = "INSERT INTO avaliacao (id, comentario, pontuacao, matricula_aluno) " +
                "VALUES (?, ?, ?, ?)"

        val reviewPreparedStatement = database.prepareStatement(reviewInsertStatement)

        reviewModel.apply {
            reviewPreparedStatement.setInt(1, reviewModel.id)
            reviewPreparedStatement.setString(2, comment)
            reviewPreparedStatement.setInt(3, rating)
            reviewPreparedStatement.setString(4, userRegistrationNumber)
        }

        reviewPreparedStatement.execute()

        val teacherReviewInsertStatement = "INSERT INTO avaliacao_professor " +
                "(id_avaliacao, nome_professor, codigo_departamento) " +
                "VALUES (?, ?, ?)"

        val teacherReviewPreparedStatement = database.prepareStatement(teacherReviewInsertStatement)

        reviewModel.apply {
            teacherReviewPreparedStatement.setInt(1, reviewModel.id)
            teacherReviewPreparedStatement.setString(2, reviewModel.teacherName)
            teacherReviewPreparedStatement.setInt(3, reviewModel.departmentCode)

        }

        teacherReviewPreparedStatement.execute()
    }

    fun getUserReviews(userRegistrationNumber: String): List<ReviewModel> {
        val reviews = mutableListOf<ReviewModel>()

        val classReviewQueryResult = database.executeQuery(
            "SELECT avaliacao_turma.*, avaliacao.*, usuario.nome AS usuario_nome, " +
            "usuario.foto_de_perfil, usuario.matricula " +
            "FROM avaliacao_turma " +
            "INNER JOIN avaliacao ON avaliacao_turma.id_avaliacao = avaliacao.id " +
            "INNER JOIN usuario ON avaliacao.matricula_aluno = usuario.matricula " +
            "WHERE usuario.matricula = $userRegistrationNumber;"
        )

        while (classReviewQueryResult.next()) {
            val profilePic = if (classReviewQueryResult.getObject("foto_de_perfil") == null) {
                null
            } else {
                val profilePicBytes = classReviewQueryResult.getBytes("foto_de_perfil")
                val bufferedProfilePicImage = ImageIO.read(profilePicBytes.inputStream())
                bufferedProfilePicImage.toComposeImageBitmap()
            }

            reviews.add(
                ClassReviewModel(
                    classReviewQueryResult.getInt("id"),
                    classReviewQueryResult.getString("comentario") ?: "",
                    classReviewQueryResult.getInt("pontuacao"),
                    profilePic,
                    classReviewQueryResult.getString("usuario_nome"),
                    classReviewQueryResult.getString("matricula"),
                    reportDAO.getReviewReports(classReviewQueryResult.getInt("id")),
                    classReviewQueryResult.getInt("id_turma"),
                )
            )
        }

        val teachersReviewQueryResult = database.executeQuery(
            "SELECT avaliacao_professor.*, avaliacao.*, usuario.nome AS usuario_nome, " +
            "usuario.foto_de_perfil, usuario.matricula " +
            "FROM avaliacao_professor " +
            "INNER JOIN avaliacao ON avaliacao_professor.id_avaliacao = avaliacao.id " +
            "INNER JOIN usuario ON avaliacao.matricula_aluno = usuario.matricula " +
            "WHERE usuario.matricula = $userRegistrationNumber;"
        )

        while (teachersReviewQueryResult.next()) {
            val profilePic = if (teachersReviewQueryResult.getObject("foto_de_perfil") == null) {
                null
            } else {
                val profilePicBytes = teachersReviewQueryResult.getBytes("foto_de_perfil")
                val bufferedProfilePicImage = ImageIO.read(profilePicBytes.inputStream())
                bufferedProfilePicImage.toComposeImageBitmap()
            }

            reviews.add(
                TeacherReviewModel(
                    teachersReviewQueryResult.getInt("id"),
                    teachersReviewQueryResult.getString("comentario") ?: "",
                    teachersReviewQueryResult.getInt("pontuacao"),
                    profilePic,
                    teachersReviewQueryResult.getString("usuario_nome"),
                    teachersReviewQueryResult.getString("matricula"),
                    reportDAO.getReviewReports(teachersReviewQueryResult.getInt("id")),
                    teachersReviewQueryResult.getString("nome_professor"),
                    teachersReviewQueryResult.getInt("codigo_departamento")
                )
            )
        }

        return reviews.toList()
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
            val profilePic = if (reviewsQueryResult.getObject("foto_de_perfil") == null) {
                null
            } else {
                val profilePicBytes = reviewsQueryResult.getBytes("foto_de_perfil")
                val bufferedProfilePicImage = ImageIO.read(profilePicBytes.inputStream())
                bufferedProfilePicImage.toComposeImageBitmap()
            }

            teacherReviews.add(
                TeacherReviewModel(
                    reviewsQueryResult.getInt("id"),
                    reviewsQueryResult.getString("comentario") ?: "",
                    reviewsQueryResult.getInt("pontuacao"),
                    profilePic,
                    reviewsQueryResult.getString("usuario_nome"),
                    reviewsQueryResult.getString("matricula"),
                    reportDAO.getReviewReports(reviewsQueryResult.getInt("id")),
                    reviewsQueryResult.getString("nome_professor"),
                    reviewsQueryResult.getInt("codigo_departamento")
                )
            )
        }

        return teacherReviews.toList()
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
            val profilePic = if (reviewsQueryResult.getObject("foto_de_perfil") == null) {
                null
            } else {
                val profilePicBytes = reviewsQueryResult.getBytes("foto_de_perfil")
                val bufferedProfilePicImage = ImageIO.read(profilePicBytes.inputStream())
                bufferedProfilePicImage.toComposeImageBitmap()
            }

            classReviews.add(
                ClassReviewModel(
                    reviewsQueryResult.getInt("id"),
                    reviewsQueryResult.getString("comentario") ?: "",
                    reviewsQueryResult.getInt("pontuacao"),
                    profilePic,
                    reviewsQueryResult.getString("usuario_nome"),
                    reviewsQueryResult.getString("matricula"),
                    reportDAO.getReviewReports(reviewsQueryResult.getInt("id")),
                    reviewsQueryResult.getInt("id_turma"),
                )
            )
        }

        return classReviews.toList()
    }

    fun userMadeReview(userRegistrationNumber: String, classId: Int): Boolean {
        val reviewQueryResult = database.executeQuery(
            "SELECT * FROM avaliacao " +
            "INNER JOIN avaliacao_turma ON avaliacao.id = avaliacao_turma.id_avaliacao " +
            "WHERE avaliacao_turma.id_turma = $classId AND avaliacao.matricula_aluno = $userRegistrationNumber"
        )

        return reviewQueryResult.next()
    }

    fun userMadeReview(userRegistrationNumber: String, teacherName: String, departmentCode: Int): Boolean {
        val reviewQueryResult = database.executeQuery(
            "SELECT * FROM avaliacao " +
                    "INNER JOIN avaliacao_professor ON avaliacao.id = avaliacao_professor.id_avaliacao " +
                    "WHERE avaliacao_professor.nome_professor = '$teacherName' AND " +
                    "avaliacao_professor.codigo_departamento = $departmentCode AND " +
                    "avaliacao.matricula_aluno = $userRegistrationNumber"
        )

        return reviewQueryResult.next()
    }

    fun updateReview(review: ReviewModel) {
        database.executeStatement(
    "UPDATE avaliacao " +
            "SET comentario = '${review.comment}', pontuacao = '${review.rating}' " +
            "WHERE id = '${review.id}'"
        )
    }

    fun deleteReview(reviewId: Int) {
        database.executeStatement(
            "DELETE FROM avaliacao WHERE id = $reviewId"
        )
    }
}