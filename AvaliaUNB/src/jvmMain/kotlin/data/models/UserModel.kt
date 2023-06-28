package data.models

import androidx.compose.ui.graphics.ImageBitmap
import org.jetbrains.skia.Picture

data class UserModel(
    val registrationNumber: String,
    val name: String,
    val course: String?,
    val email: String,
    val password: String,
    val profilePicture: ImageBitmap? = null
)