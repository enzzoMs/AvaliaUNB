package ui.screens.subjects.all.viewmodel

import data.models.SubjectModel
import data.repositories.SemesterRepository
import data.repositories.SubjectRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import javax.inject.Inject

class SubjectsViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    semesterRepository: SemesterRepository
) {
    private var allSubjects = listOf<SubjectModel>()
    private val _subjectUiState: MutableStateFlow<SubjectsUiState>
    val subjectUiState: StateFlow<SubjectsUiState>

    init {
        val semesters = semesterRepository.getAllSemesters().map { semesterModel ->
            "${semesterModel.year}-${semesterModel.semesterNumber}"
        }

        _subjectUiState = MutableStateFlow(
            SubjectsUiState(
                subjects = allSubjects,
                isSubjectsLoading = true,
                departmentNames = allSubjects.map { it.departmentName }.distinct(),
                semesters = semesters,
                semesterFilter = semesters.first()
            )
        )

        subjectUiState = _subjectUiState.asStateFlow()

        loadAllSubjects()
    }

    private fun loadAllSubjects() {
        CoroutineScope(Dispatchers.IO).launch {
            allSubjects = subjectRepository.getAllSubjects()
            _subjectUiState.update { subjectUiState ->
                subjectUiState.copy(
                    subjects = filterSubjects(null, null, subjectUiState.semesters.first()),
                    departmentNames = allSubjects.map { it.departmentName }.distinct(),
                    isSubjectsLoading = false
                )
            }
        }
    }


    fun updateSearchSubjectFilter(newSearchFilter: String?) {
        _subjectUiState.update { subjectUiState ->
            subjectUiState.copy(
                searchSubjectFilter = newSearchFilter,
                subjects = filterSubjects(searchSubjectFilter = newSearchFilter)
            )
        }
    }

    fun updateDepartmentFilter(newDeptFilter: String?) {
        _subjectUiState.update { subjectUiState ->
            subjectUiState.copy(
                departmentFilter = newDeptFilter,
                subjects = filterSubjects(deptFilter = newDeptFilter)
            )
        }
    }

    fun updateSemesterFilter(newSemesterFilter: String) {
        _subjectUiState.update { subjectUiState ->
            subjectUiState.copy(
                semesterFilter = newSemesterFilter,
                subjects = filterSubjects(semesterFilter = newSemesterFilter)
            )
        }
    }


    private fun filterSubjects(
        searchSubjectFilter: String? = _subjectUiState.value.searchSubjectFilter,
        deptFilter: String? = _subjectUiState.value.departmentFilter,
        semesterFilter: String = _subjectUiState.value.semesterFilter
    ): List<SubjectModel> {
        return allSubjects.filter { subject ->
            (searchSubjectFilter == null || subject.name.contains(searchSubjectFilter, ignoreCase = true))
                    && (deptFilter == null || subject.departmentName == deptFilter)
                    && (subject.semester == semesterFilter)
        }
    }
}