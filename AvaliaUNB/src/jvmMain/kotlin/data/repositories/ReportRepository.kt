package data.repositories

import data.models.ReportModel
import data.source.ReportDAO
import data.source.ReviewDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepository @Inject constructor(
    private val reportDAO: ReportDAO
) {

    fun insertReport(reportModel: ReportModel) = reportDAO.insertReport(reportModel)

    fun getUserReport(reviewId: Int, userRegistrationNumber: String): ReportModel? {
        return reportDAO.getUserReport(reviewId, userRegistrationNumber)
    }

    fun updateReport(reviewId: Int, userRegistrationNumber: String, newDescription: String) {
        reportDAO.updateReport(reviewId, userRegistrationNumber, newDescription)
    }

    fun deleteReport(reviewId: Int, userRegistrationNumber: String) {
        reportDAO.deleteReport(reviewId, userRegistrationNumber)
    }

}
