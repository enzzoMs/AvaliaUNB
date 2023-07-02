package ui.screens.subjects.viewmodel

import data.models.SubjectModel
import data.repositories.SubjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import javax.inject.Inject

private const val SEMESTER_2022_1 = "2022-1"
private const val SEMESTER_2022_2 = "2022-2"
private const val SEMESTER_2023_1 = "2023-1"

class SubjectsViewModel @Inject constructor(
    subjectRepository: SubjectRepository
) {
    private val allSubjects = subjectRepository.getAllSubjects()

    private val _subjectUiState = MutableStateFlow(
        SubjectsUiState(
            subjects = allSubjects,
            departmentNames = allSubjects.map { it.departmentName }.distinct(),
            semesters = listOf(
                SEMESTER_2022_1,
                SEMESTER_2022_2,
                SEMESTER_2023_1
            )
        )
    )
    val subjectUiState = _subjectUiState.asStateFlow()

    fun updateSearchSubjectFilter(newSearchFilter: String?) {
        _subjectUiState.update { subjectUiState ->
            subjectUiState.copy(
                searchSubjectFilter = newSearchFilter,
                subjects = filterSubjects(newSearchFilter, subjectUiState.departmentFilter, subjectUiState.semesterFilter)
            )
        }
    }

    fun updateDepartmentFilter(newDeptFilter: String?) {
        _subjectUiState.update { subjectUiState ->
            subjectUiState.copy(
                departmentFilter = newDeptFilter,
                subjects = filterSubjects(subjectUiState.searchSubjectFilter, newDeptFilter, subjectUiState.semesterFilter)
            )
        }
    }

    fun updateSemesterFilter(newSemesterFilter: String?) {
        _subjectUiState.update { subjectUiState ->
            subjectUiState.copy(
                semesterFilter = newSemesterFilter,
                subjects = filterSubjects(
                    subjectUiState.searchSubjectFilter, subjectUiState.departmentFilter, newSemesterFilter
                )
            )
        }
    }


    private fun filterSubjects(searchSubjectFilter: String?, deptFilter: String?, semesterFilter: String?): List<SubjectModel> {
        return allSubjects.filter { subject ->
            (searchSubjectFilter == null || subject.name.contains(searchSubjectFilter, ignoreCase = true))
                    && (deptFilter == null || subject.departmentName == deptFilter)
                    && (semesterFilter == null || subject.semester == semesterFilter)
        }
    }
}