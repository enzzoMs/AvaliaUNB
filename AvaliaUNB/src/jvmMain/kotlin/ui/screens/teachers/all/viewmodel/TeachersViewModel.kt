package ui.screens.teachers.all.viewmodel

import data.models.TeacherModel
import data.repositories.SemesterRepository
import data.repositories.TeacherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import utils.resources.Strings
import javax.inject.Inject

class TeachersViewModel @Inject constructor(
    private val teacherRepository: TeacherRepository,
    semesterRepository: SemesterRepository
) {
    private var allTeachers = listOf<TeacherModel>()
    private val _teachersUiState: MutableStateFlow<TeachersUiState>
    val teachersUiState: StateFlow<TeachersUiState>

    init {
        val semesters = semesterRepository.getAllSemesters().map { semesterModel ->
            "${semesterModel.year}-${semesterModel.semesterNumber}"
        }

        _teachersUiState = MutableStateFlow(
            TeachersUiState(
                teachers = allTeachers,
                isTeachersLoading = true,
                departmentNames = listOf(),
                semesters = semesters,
                semesterFilter = semesters.first(),
                ratingFilters = Strings.LIST_RATING_FILTERS
            )
        )

        teachersUiState = _teachersUiState.asStateFlow()

        loadAllTeachers()
    }

    private fun loadAllTeachers() {
        CoroutineScope(Dispatchers.IO).launch {
            allTeachers = teacherRepository.getAllTeachers()

            _teachersUiState.update { teacherUiState ->
                teacherUiState.copy(
                    teachers = filterTeachers(null, null, teacherUiState.semesters.first()),
                    departmentNames = allTeachers.map { it.departmentName }.distinct(),
                    isTeachersLoading = false
                )
            }
        }
    }

    fun updateSearchTeacherNameFilter(newSearchFilter: String?) {
        _teachersUiState.update { teacherUiState ->
            teacherUiState.copy(
                searchTeacherNameFilter = newSearchFilter,
                teachers = filterTeachers(searchTeacherNameFilter = newSearchFilter)
            )
        }
    }

    fun updateDepartmentFilter(newDeptFilter: String?) {
        _teachersUiState.update { teacherUiState ->
            teacherUiState.copy(
                departmentFilter = newDeptFilter,
                teachers = filterTeachers(deptFilter = newDeptFilter)
            )
        }
    }

    fun updateSemesterFilter(newSemesterFilter: String) {
        _teachersUiState.update { teacherUiState ->
            teacherUiState.copy(
                semesterFilter = newSemesterFilter,
                teachers = filterTeachers(semesterFilter = newSemesterFilter)
            )
        }
    }

    fun updateRatingFilter(newRatingFilter: String?) {
        val ratingFilterNumber = newRatingFilter?.find { it.isDigit() }?.digitToInt()

        _teachersUiState.update { teacherUiState ->
            teacherUiState.copy(
                currentRatingFilter = newRatingFilter,
                teachers = filterTeachers(ratingFilter = ratingFilterNumber)
            )
        }
    }

    private fun filterTeachers(
        searchTeacherNameFilter: String? = _teachersUiState.value.searchTeacherNameFilter,
        deptFilter: String? = _teachersUiState.value.departmentFilter,
        semesterFilter: String = _teachersUiState.value.semesterFilter,
        ratingFilter: Int? = _teachersUiState.value.currentRatingFilter?.find { it.isDigit() }?.digitToInt()
    ): List<TeacherModel> {
        return allTeachers.filter { teacherModel ->
            val teacherScoreRounded = teacherModel.score?.toInt()

            (searchTeacherNameFilter == null || teacherModel.name.contains(searchTeacherNameFilter, ignoreCase = true))
                    && (deptFilter == null || teacherModel.departmentName == deptFilter)
                    && (teacherModel.semester == semesterFilter)
                    && (ratingFilter == null ||
                    (teacherScoreRounded != null && teacherScoreRounded >= ratingFilter))
        }
    }

}


