package ui.screens.subjects.all.viewmodel

import data.models.SubjectModel

data class SubjectsUiState(
    val subjects: List<SubjectModel>,
    val searchSubjectFilter: String? = null,
    val departmentNames: List<String> = listOf(),
    val departmentFilter: String? = null,
    val semesters: List<String> = listOf(),
    val semesterFilter: String? = null
)
