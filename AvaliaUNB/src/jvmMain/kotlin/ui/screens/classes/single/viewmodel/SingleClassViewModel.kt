package ui.screens.classes.single.viewmodel

import data.models.ClassModel
import data.models.ClassReviewModel
import data.models.ReviewModel
import data.models.UserModel
import data.repositories.ClassRepository
import data.repositories.ReviewRepository
import data.repositories.ReviewRepository.ReviewInsertionResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

class SingleClassViewModel(
    private val classModel: ClassModel,
    private val user: UserModel,
    private val classRepository: ClassRepository,
    private val reviewRepository: ReviewRepository
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

    fun reviewBelongsToUser(review: ReviewModel): Boolean = review.userRegistrationNumber == user.registrationNumber

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
            userProfilePicture = user.profilePicture,
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