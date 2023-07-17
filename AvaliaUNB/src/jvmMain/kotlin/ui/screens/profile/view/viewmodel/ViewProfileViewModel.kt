package ui.screens.profile.view.viewmodel

import data.models.ReportModel
import data.models.ReviewModel
import data.repositories.ReportRepository
import data.repositories.ReviewRepository
import data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import utils.Utils

class ViewProfileViewModel(
    userRegistrationNumber: String,
    private val reviewRepository: ReviewRepository,
    private val reportRepository: ReportRepository,
    private val userRepository: UserRepository,
) {
    private val userModel = userRepository.getUser(userRegistrationNumber)

    private val _viewProfileUiState = MutableStateFlow(
        userModel.run {
            ViewProfileUiState(
                registrationNumber,
                name,
                course,
                email,
                profilePicture ?: Utils.getDefaultProfilePicture(),
                reportRepository.userWasReported(registrationNumber),
                reviewRepository.getUserReviews(registrationNumber).filter { it.reports.isNotEmpty() }
            )
        }
    )
    val viewProfileUiState = _viewProfileUiState.asStateFlow()

    fun deleteUser() {
        userRepository.delete(userModel.registrationNumber)
    }


    fun deleteReport(reportModel: ReportModel) {
        reportRepository.deleteReport(reportModel.reviewId, reportModel.userRegistrationNumber)

        val newReviews = reviewRepository.getUserReviews(userModel.registrationNumber).filter { it.reports.isNotEmpty() }

        _viewProfileUiState.update { viewProfileUiState ->
            viewProfileUiState.copy(
                userReviews = newReviews,
                showDeleteButton = newReviews.isNotEmpty()
            )
        }
    }

    fun deleteReview(review: ReviewModel) {
        reviewRepository.deleteReview(review.id)

        _viewProfileUiState.update { viewProfileUiState ->
            viewProfileUiState.copy(
                userReviews = viewProfileUiState.userReviews.filter { it.id != review.id }
            )
        }
    }
}