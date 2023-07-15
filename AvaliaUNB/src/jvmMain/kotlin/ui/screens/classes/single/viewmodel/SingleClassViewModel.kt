package ui.screens.classes.single.viewmodel

import data.models.*
import data.repositories.ClassRepository
import data.repositories.ReportRepository
import data.repositories.ReviewRepository
import data.repositories.ReviewRepository.ReviewInsertionResult
import data.repositories.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import utils.Utils
import java.util.*

class SingleClassViewModel(
    private val classModel: ClassModel,
    private val user: UserModel,
    private val classRepository: ClassRepository,
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository,
    private val reportRepository: ReportRepository
) {
    private val _singleClassUiState = MutableStateFlow(
        SingleClassUiState(
            classModel = classModel,
            teacherModel = classRepository.getClassTeacher(classModel),
            reviews = listOf(),
            isReviewsLoading = true
        )
    )

    val singleClassUiState = _singleClassUiState.asStateFlow()

    init {
        loadAllReviews()
    }

    private fun loadAllReviews() {
        CoroutineScope(Dispatchers.IO).launch {
            val allReviews = classRepository.getClassReviews(classModel)

            _singleClassUiState.update { singleClassUiState ->
                singleClassUiState.copy(
                    reviews = allReviews,
                    isReviewsLoading = false
                )
            }
        }
    }

    fun reviewBelongsToUser(review: ReviewModel): Boolean = review.userRegistrationNumber == user.registrationNumber

    fun reportBelongsToUser(report: ReportModel): Boolean = report.userRegistrationNumber == user.registrationNumber

    fun getUserReport(review: ReviewModel): ReportModel? = reportRepository.getUserReport(review.id, user.registrationNumber)

    fun userIsAdministrator(): Boolean = userRepository.isUserAdministrator(user.registrationNumber)

    fun reviewHasReports(reviewModel: ReviewModel): Boolean = getReviewReports(reviewModel).isNotEmpty()

    private fun getReviewReports(reviewModel: ReviewModel): List<ReportModel> =  reportRepository.getReviewReports(reviewModel.id)

    private fun updateReports(reviewId: Int) {
        _singleClassUiState.update { singleClassUiState ->
            singleClassUiState.copy(
                reviews = singleClassUiState.reviews.map {
                    if (it.id == reviewId) {
                        it.copy(reports = getReviewReports(it))
                    } else {
                        it
                    }
                }
            )
        }
    }


    fun updateReviewComment(newComment: String) {
        _singleClassUiState.update { singleClassUiState ->
            singleClassUiState.copy(
                reviewComment = newComment,
                userAlreadyMadeReview = false
            )
        }
    }

    fun publishReview(comment: String, rating: Int) {
        val reviewModel = ClassReviewModel(
            id = UUID.randomUUID().hashCode(),
            comment = comment,
            rating = rating,
            userProfilePicture = user.profilePicture ?: Utils.getDefaultProfilePicture(),
            userName = user.name,
            userRegistrationNumber = user.registrationNumber,
            classId = _singleClassUiState.value.classModel.id,
            reports = listOf()
        )

        val insertionResult = reviewRepository.insertReview(reviewModel)

        when (insertionResult) {
            ReviewInsertionResult.ReviewAlreadyMade -> {
                _singleClassUiState.update { singleClassUiState ->
                    singleClassUiState.copy(
                        userAlreadyMadeReview = true
                    )
                }
            }
            ReviewInsertionResult.Success -> {
                _singleClassUiState.update { singleClassUiState ->
                    singleClassUiState.copy(
                        reviews = listOf(reviewModel) + singleClassUiState.reviews,
                        classModel = classModel.copy(
                            score = classRepository.getClassScore(classModel.id)
                        ),
                        reviewComment = ""
                    )
                }
            }
        }
    }

    fun submitReviewReport(reviewId: Int, reviewDescription: String) {
        val reportModel = ReportModel(
            reviewId = reviewId,
            userRegistrationNumber = user.registrationNumber,
            description = reviewDescription,
            userName = user.name
        )

        reportRepository.insertReport(reportModel)

        updateReports(reviewId)
    }

    fun editReport(oldReportModel: ReportModel, newDescription: String) {
        reportRepository.updateReport(oldReportModel.reviewId, oldReportModel.userRegistrationNumber, newDescription)

        updateReports(oldReportModel.reviewId)
    }

    fun deleteReport(reportModel: ReportModel) {
        reportRepository.deleteReport(reportModel.reviewId, reportModel.userRegistrationNumber)

        updateReports(reportModel.reviewId)
    }


    fun editReview(oldReviewModel: ReviewModel, newRating: Int, newComment: String) {
        val newReview = (oldReviewModel as ClassReviewModel).copy(
            rating = newRating,
            comment = newComment
        )

        reviewRepository.updateReview(newReview)

        _singleClassUiState.update { singleClassUiState ->
            singleClassUiState.copy(
                reviews = singleClassUiState.reviews.map { if (it == oldReviewModel) newReview else it },
                classModel = classModel.copy(
                    score = classRepository.getClassScore(classModel.id)
                )
            )
        }
    }

    fun deleteReview(review: ClassReviewModel) {
        reviewRepository.deleteReview(review.id)

        _singleClassUiState.update { singleClassUiState ->
            singleClassUiState.copy(
                reviews = singleClassUiState.reviews.filter { it != review },
                classModel = classModel.copy(
                    score = classRepository.getClassScore(classModel.id)
                ),
                userAlreadyMadeReview = false
            )
        }
    }
}