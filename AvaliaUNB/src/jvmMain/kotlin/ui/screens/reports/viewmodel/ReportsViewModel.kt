package ui.screens.reports.viewmodel

import data.models.ReportModel
import data.models.ReviewModel
import data.repositories.ReportRepository
import data.repositories.ReviewRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReportsViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val reportRepository: ReportRepository
) {

    private val _reportsUiState = MutableStateFlow(
        ReportsUiState(
            reviews = listOf(),
            isReviewsLoading = true
        )
    )

    val reportsUiState = _reportsUiState.asStateFlow()

    init {
        loadAllReports()
    }

    private fun loadAllReports() {
        CoroutineScope(Dispatchers.IO).launch {
            val allReviews = reviewRepository.getAllReportedReviews()

            _reportsUiState.update { reportsUiState ->
                reportsUiState.copy(
                    reviews = allReviews,
                    isReviewsLoading = false
                )
            }
        }
    }

    fun deleteReport(reportModel: ReportModel) {
        reportRepository.deleteReport(reportModel.reviewId, reportModel.userRegistrationNumber)

        _reportsUiState.update { reportsUiState ->
            reportsUiState.copy(
                reviews = reviewRepository.getAllReportedReviews()
            )
        }

    }

    fun deleteReview(review: ReviewModel) {
        reviewRepository.deleteReview(review.id)

        _reportsUiState.update { reportsUiState ->
            reportsUiState.copy(
                reviews = reportsUiState.reviews.filter { it.id != review.id }
            )
        }
    }
}
