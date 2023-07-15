package data.models

import androidx.compose.ui.graphics.ImageBitmap

data class TeacherReviewModel(
    override val id: Int,
    override val comment: String,
    override val rating: Int,
    override val userProfilePicture: ImageBitmap? = null,
    override val userName: String,
    override val userRegistrationNumber: String,
    override val reports: List<ReportModel>,
    val teacherName: String,
    val departmentCode: Int,
): ReviewModel
