package data.source

import data.models.ClassReviewModel
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

    fun userMadeReview(userRegistrationNumber: String, classId: Int): Boolean {
        val reviewQueryResult = database.executeQuery(
            "SELECT * FROM avaliacao " +
                    "INNER JOIN avaliacao_turma ON avaliacao.id = avaliacao_turma.id_avaliacao " +
                    "WHERE avaliacao_turma.id_turma = $classId AND avaliacao.matricula_aluno = $userRegistrationNumber"
        )

        return reviewQueryResult.next()
    }

    fun updateClassReview(review: ClassReviewModel) {
        database.executeStatement(
    "UPDATE avaliacao " +
            "SET comentario = '${review.comment}', pontuacao = '${review.rating}' " +
            "WHERE id = '${review.id}'"
        )
    }

    /*
    fun insertTeacherReview(reviewModel: ) {

    }*/

    fun deleteReview(reviewId: Int) {
        database.executeStatement(
            "DELETE FROM avaliacao WHERE id = $reviewId"
        )

        database.executeStatement(
            "DELETE FROM avaliacao_turma WHERE id_avaliacao = $reviewId"
        )
    }
}