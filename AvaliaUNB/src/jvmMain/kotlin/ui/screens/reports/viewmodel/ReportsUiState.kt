package ui.screens.reports.viewmodel

import data.models.ReviewModel

data class ReportsUiState(
    val reviews: List<ReviewModel>,
    val isReviewsLoading: Boolean
)
