package data.repositories

import data.models.ClassReviewModel
import data.models.ReviewModel
import data.models.TeacherReviewModel
import data.source.ReviewDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewRepository @Inject constructor(
    private val reviewDAO: ReviewDAO
) {
    fun insertReview(reviewModel: ReviewModel): ReviewInsertionResult {
        return when(reviewModel) {
            is ClassReviewModel -> {
                val userMadeReview = reviewDAO.userMadeReview(reviewModel.userRegistrationNumber, reviewModel.classId)

                if (userMadeReview) {
                    ReviewInsertionResult.ReviewAlreadyMade
                } else {
                    reviewDAO.insertClassReview(reviewModel)
                    ReviewInsertionResult.Success
                }
            }
            is TeacherReviewModel -> {
                val userMadeReview = reviewDAO.userMadeReview(
                    reviewModel.userRegistrationNumber,
                    reviewModel.teacherName,
                    reviewModel.departmentCode
                )

                if (userMadeReview) {
                    ReviewInsertionResult.ReviewAlreadyMade
                } else {
                    reviewDAO.insertTeacherReview(reviewModel)
                    ReviewInsertionResult.Success
                }
            }
            else -> error("Review belongs to a unknown type")
        }
    }

    fun updateReview(review: ReviewModel) = reviewDAO.updateReview(review)

    fun deleteReview(reviewId: Int) = reviewDAO.deleteReview(reviewId)

    enum class ReviewInsertionResult {
        Success,
        ReviewAlreadyMade
    }
}