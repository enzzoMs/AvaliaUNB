package data.models

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import utils.resources.ResourcesUtils
import java.io.File
import javax.imageio.ImageIO

data class TeacherModel(
    val name: String,
    val departmentName: String,
    val departmentCode: Int,
    val semester: String,
    val score: Double?,
    val numOfReviews: Int = 0,
    val profilePicture: ImageBitmap = ImageIO.read(File(ResourcesUtils.ImagePaths.PERSON)).toComposeImageBitmap()
)
