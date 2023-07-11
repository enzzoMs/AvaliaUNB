package ui.screens.classes.all.viewmodel

import data.models.ClassModel

data class ClassesUiState(
    val classes: List<ClassModel>,
    val isClassesLoading: Boolean,
    val searchSubjectFilter: String? = null,
    val departmentNames: List<String> = listOf(),
    val departmentFilter: String? = null,
    val semesters: List<String> = listOf(),
    val semesterFilter: String,
    val ratings: List<String> = listOf(),
    val ratingFilters: List<String>,
    val currentRatingFilter: String? = null
)
