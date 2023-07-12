package data.source

import data.models.ReportModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportDAO @Inject constructor(
    private val database: DatabaseManager
) {

    fun insertReport(reportModel: ReportModel) {
        val reportInsertStatement = database.prepareStatement(
    "INSERT INTO denuncia (id_avaliacao, matricula_aluno, descricao) " +
            "VALUES (?, ?, ?)"
        )

        reportInsertStatement.setInt(1, reportModel.reviewId)
        reportInsertStatement.setString(2, reportModel.userRegistrationNumber)
        reportInsertStatement.setString(3, reportModel.description)

        reportInsertStatement.execute()
    }

    fun updateReport(reviewId: Int, userRegistrationNumber: String, newDescription: String) {
        database.executeStatement(
            "UPDATE denuncia " +
            "SET descricao = '$newDescription' " +
            "WHERE id_avaliacao = '$reviewId' AND matricula_aluno = $userRegistrationNumber"
        )
    }

    fun deleteReport(reviewId: Int, userRegistrationNumber: String) {
        database.executeStatement(
            "DELETE FROM denuncia " +
            "WHERE id_avaliacao = $reviewId AND matricula_aluno = $userRegistrationNumber"
        )
    }

    fun getUserReport(reviewId: Int, userRegistrationNumber: String): ReportModel? {
        val reportQueryResult = database.executeQuery(
            "SELECT * FROM denuncia " +
            "WHERE matricula_aluno = $userRegistrationNumber AND id_avaliacao = $reviewId"
        )

        return if (reportQueryResult.next()) {
            return ReportModel(
                reviewId = reviewId,
                userRegistrationNumber = userRegistrationNumber,
                reportQueryResult.getString("descricao")
            )
        } else {
            null
        }
    }
}