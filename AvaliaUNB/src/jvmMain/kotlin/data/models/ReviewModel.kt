package data.models

import androidx.compose.ui.graphics.ImageBitmap

interface ReviewModel {
    val comment: String
    val rating: Int
    val userProfilePicture: ImageBitmap
    val userName: String
    val userRegistrationNumber: String
    val classId: Int
}
