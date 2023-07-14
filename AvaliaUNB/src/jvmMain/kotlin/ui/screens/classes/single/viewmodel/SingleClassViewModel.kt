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
            val allReviews = classRepository.getClassReviews(classModel).reversed()

            _singleClassUiState.update { singleClassUiState ->
                singleClassUiState.copy(
                    reviews = allReviews,
                    isReviewsLoading = false
                )
            }
        }
    }

    fun getUserRegistrationNumber(): String = user.registrationNumber

    fun reviewBelongsToUser(review: ReviewModel): Boolean = review.userRegistrationNumber == user.registrationNumber

    fun getUserReport(review: ReviewModel): ReportModel? = reportRepository.getUserReport(review.id, user.registrationNumber)

    fun userIsAdministrator(): Boolean = userRepository.isUserAdministrator(user.registrationNumber)

    fun getReviewReports(reviewModel: ReviewModel): List<ReportModel> =  reportRepository.getReviewReports(reviewModel.id)


    fun updateReviewComment(newComment: String) {
        _singleClassUiState.update { singleClassUiState ->
            singleClassUiState.copy(
                reviewComment = newComment,
                userAlreadyMadeReview = false
            )
        }
    }

    fun deleteReport(reviewId: Int) = reportRepository.deleteReport(reviewId, user.registrationNumber)

    fun deleteReport(reportModel: ReportModel) = reportRepository.deleteReport(reportModel.reviewId, reportModel.userRegistrationNumber)

    fun publishReview(comment: String, rating: Int) {
        val reviewModel = ClassReviewModel(
            id = UUID.randomUUID().hashCode(),
            comment = comment,
            rating = rating,
            userProfilePicture = user.profilePicture ?: Utils.getDefaultProfilePicture(),
            userName = user.name,
            userRegistrationNumber = user.registrationNumber,
            classId = _singleClassUiState.value.classModel.id
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
        reportRepository.insertReport(
            ReportModel(
                reviewId = reviewId,
                userRegistrationNumber = user.registrationNumber,
                description = reviewDescription,
                userName = user.name
            )
        )
    }

    fun editReport(reviewId: Int, newDescription: String) {
        reportRepository.updateReport(reviewId, user.registrationNumber, newDescription)
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