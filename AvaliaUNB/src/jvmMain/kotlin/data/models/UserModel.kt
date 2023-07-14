package data.models

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import utils.resources.Paths
import java.io.File
import javax.imageio.ImageIO

data class UserModel(
    val registrationNumber: String,
    val name: String,
    val course: String?,
    val email: String,
    val password: String,
    val profilePicture: ImageBitmap = ImageIO.read(File(Paths.Images.PERSON)).toComposeImageBitmap(),
    val isAdministrator: Boolean = false
)