package data.models

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import java.io.File
import javax.imageio.ImageIO

private const val DEFAULT_PROFILE_PIC_PATH = "src/jvmMain/resources/images/person.png"
val defaultProfilePicture = ImageIO.read(File(DEFAULT_PROFILE_PIC_PATH)).toComposeImageBitmap()

data class UserModel(
    val registrationNumber: String,
    val name: String,
    val course: String?,
    val email: String,
    val password: String,
    val profilePicture: ImageBitmap = defaultProfilePicture
)