package ui.screens.subjects.all.viewmodel

import data.models.SubjectModel
import data.repositories.SemesterRepository
import data.repositories.SubjectRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import javax.inject.Inject

class SubjectsViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    semesterRepository: SemesterRepository
) {
    private var allSubjects = listOf<SubjectModel>()

    private val _subjectUiState = MutableStateFlow(
        SubjectsUiState(
            subjects = allSubjects,
            departmentNames = allSubjects.map { it.departmentName }.distinct(),
            semesters = semesterRepository.getAllSemesters().map { semesterModel ->
                "${semesterModel.year}-${semesterModel.semesterNumber}"
            }
        )
    )
    val subjectUiState = _subjectUiState.asStateFlow()

    init {
        loadAllSubjects()
    }

    private fun loadAllSubjects() {
        CoroutineScope(Dispatchers.IO).launch {
            allSubjects = subjectRepository.getAllSubjects()
            _subjectUiState.update { subjectUiState ->
                subjectUiState.copy(
                    subjects = allSubjects,
                    departmentNames = allSubjects.map { it.departmentName }.distinct()
                )
            }
        }
    }


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