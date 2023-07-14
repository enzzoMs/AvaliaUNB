package ui.screens.teachers.all.viewmodel

import data.models.TeacherModel

data class TeachersUiState(
    val teachers: List<TeacherModel>,
    val isTeachersLoading: Boolean,
    val searchTeacherNameFilter: String? = null,
    val departmentNames: List<String> = listOf(),
    val departmentFilter: String? = null,
    val semesters: List<String> = listOf(),
    val semesterFilter: String,
    val ratings: List<String> = listOf(),
    val ratingFilters: List<String>,
    val currentRatingFilter: String? = null
)