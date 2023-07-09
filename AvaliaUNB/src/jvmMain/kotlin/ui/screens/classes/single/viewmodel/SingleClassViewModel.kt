package ui.screens.classes.single.viewmodel

import data.models.ClassModel
import data.models.ClassReviewModel
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
            val allReviews = classRepository.getClassReviews(classModel)

            _singleClassUiState.update { singleClassUiState ->
                singleClassUiState.copy(
                    reviews = allReviews,
                    isReviewsLoading = false
                )
            }
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
            comment = comment,
            rating = rating,
            userProfilePicture = user.profilePicture,
            userName = user.name,
            userRegistrationNumber = user.registrationNumber,
            classId = _singleClassUiState.value.classModel.id
        )

        val insertionResult = reviewRepository.insetReview(reviewModel)

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
}