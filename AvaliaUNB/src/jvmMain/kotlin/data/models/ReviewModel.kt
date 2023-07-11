package data.models

import androidx.compose.ui.graphics.ImageBitmap

interface ReviewModel {
    val id: Int
    val comment: String
    val rating: Int
    val userProfilePicture: ImageBitmap
    val userName: String
    val userRegistrationNumber: String
}
