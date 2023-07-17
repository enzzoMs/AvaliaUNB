package data.repositories

import data.models.ReportModel
import data.source.ReportDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepository @Inject constructor(
    private val reportDAO: ReportDAO
) {

    fun insertReport(reportModel: ReportModel) = reportDAO.insertReport(reportModel)

    fun getUserReviewReport(reviewId: Int, userRegistrationNumber: String): ReportModel? {
        return reportDAO.getUserReviewReport(reviewId, userRegistrationNumber)
    }

    fun userWasReported(userRegistrationNumber: String): Boolean {
        return reportDAO.userWasReported(userRegistrationNumber)
    }

    fun updateReport(reviewId: Int, userRegistrationNumber: String, newDescription: String) {
        reportDAO.updateReport(reviewId, userRegistrationNumber, newDescription)
    }

    fun getReviewReports(reviewId: Int): List<ReportModel> {
        return reportDAO.getReviewReports(reviewId)
    }

    fun deleteReport(reviewId: Int, userRegistrationNumber: String) {
        reportDAO.deleteReport(reviewId, userRegistrationNumber)
    }

}
