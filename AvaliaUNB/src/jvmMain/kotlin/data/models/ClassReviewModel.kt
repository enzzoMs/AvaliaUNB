package data.models

data class ClassReviewModel(
    override val comment: String,
    override val score: Int
): ReviewModel
