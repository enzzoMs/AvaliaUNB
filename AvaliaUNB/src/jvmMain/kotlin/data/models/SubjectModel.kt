package data.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent

data class SubjectModel(
    val code: String,
    val name: String,
    val semester: String,
    val departmentName: String,
    val departmentColor: Color = Transparent
)
