package data.models

import androidx.compose.ui.graphics.ImageBitmap

data class ClassReviewModel(
    override val id: Int,
    override val comment: String,
    override val rating: Int,
    override val userProfilePicture: ImageBitmap? = null,
    override val userName: String,
    override val userRegistrationNumber: String,
    val classId: Int
): ReviewModel
