package ui.screens.profile.view.viewmodel

import androidx.compose.ui.graphics.ImageBitmap
import data.models.ReviewModel

data class ViewProfileUiState(
    val registrationNumber: String = "",
    val name: String = "",
    val course: String? = null,
    val email: String = "",
    val profilePic: ImageBitmap,
    val showDeleteButton: Boolean,
    val userReviews: List<ReviewModel>
)
