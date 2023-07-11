package data.source

import data.models.ClassReviewModel
import data.models.ReviewModel
import data.models.TeacherReviewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewDAO @Inject constructor(
    private val database: DatabaseManager
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

        database.executeStatement(
            "DELETE FROM avaliacao_turma WHERE id_avaliacao = $reviewId"
        )

        database.executeStatement(
            "DELETE FROM avaliacao_professor WHERE id_avaliacao = $reviewId"
        )
    }
}