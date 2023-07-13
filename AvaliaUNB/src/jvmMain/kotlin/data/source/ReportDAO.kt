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

    fun getReviewReports(reviewId: Int): List<ReportModel> {
        val reportQueryResult = database.executeQuery(
    "SELECT denuncia.*, usuario.nome AS usuario_nome " +
            "FROM denuncia " +
            "INNER JOIN usuario ON " +
            "usuario.matricula = denuncia.matricula_aluno " +
            "WHERE id_avaliacao = $reviewId"
        )

        val reports = mutableListOf<ReportModel>()

        while (reportQueryResult.next()) {
            reports.add(
                ReportModel(
                    reviewId,
                    reportQueryResult.getString("matricula_aluno"),
                    reportQueryResult.getString("descricao"),
                    reportQueryResult.getString("usuario_nome")
                )
            )
        }

        return reports.toList()
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
    "SELECT denuncia.*, usuario.nome AS usuario_nome " +
            "FROM denuncia " +
            "INNER JOIN usuario ON " +
            "usuario.matricula = denuncia.matricula_aluno " +
            "WHERE matricula_aluno = $userRegistrationNumber AND id_avaliacao = $reviewId"
        )

        return if (reportQueryResult.next()) {
            return ReportModel(
                reviewId,
                userRegistrationNumber,
                reportQueryResult.getString("descricao"),
                reportQueryResult.getString("matricula_aluno")
            )
        } else {
            null
        }
    }
}