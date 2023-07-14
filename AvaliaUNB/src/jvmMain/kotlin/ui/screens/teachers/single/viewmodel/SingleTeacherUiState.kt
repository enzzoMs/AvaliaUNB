package ui.screens.teachers.single.viewmodel

import data.models.TeacherModel
import data.models.TeacherReviewModel

data class SingleTeacherUiState(
    val teacherModel: TeacherModel,
    val reviewComment: String = "",
    val reviews: List<TeacherReviewModel>,
    val isReviewsLoading: Boolean,
    val userAlreadyMadeReview: Boolean = false
)
