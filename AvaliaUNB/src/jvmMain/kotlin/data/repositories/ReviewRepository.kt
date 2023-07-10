package data.repositories

import data.models.ClassReviewModel
import data.models.ReviewModel
import data.source.ReviewDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewRepository @Inject constructor(
    private val reviewDAO: ReviewDAO
) {
    fun insertReview(reviewModel: ReviewModel): ReviewInsertionResult {
        val userMadeReview = reviewDAO.userMadeReview(reviewModel.userRegistrationNumber, reviewModel.classId)

        return when {
            userMadeReview -> ReviewInsertionResult.ReviewAlreadyMade
            else -> {
                when(reviewModel) {
                    is ClassReviewModel -> reviewDAO.insertClassReview(reviewModel)
                }
                ReviewInsertionResult.Success
            }
        }
    }

    fun updateReview(review: ClassReviewModel) = reviewDAO.updateClassReview(review)

    fun deleteReview(reviewId: Int) = reviewDAO.deleteReview(reviewId)

    enum class ReviewInsertionResult {
        Success,
        ReviewAlreadyMade
    }
}