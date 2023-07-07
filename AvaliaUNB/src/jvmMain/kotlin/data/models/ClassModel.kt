package data.models

import androidx.compose.ui.graphics.Color

data class ClassModel(
    val subjectName: String,
    val departmentName: String,
    val code: String,
    val schedule: String,
    val numOfHours: Int,
    val filledSeats: Int,
    val totalSeats: Int,
    val location: String,
    val teacherName: String,
    val semester: SemesterModel,
    val departmentColor: Color = Color.Transparent,
    val score: Int?
)
