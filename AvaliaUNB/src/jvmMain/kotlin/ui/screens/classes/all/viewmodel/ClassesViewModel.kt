package ui.screens.classes.all.viewmodel

import data.models.ClassModel
import data.repositories.ClassRepository
import data.repositories.SemesterRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import utils.resources.ResourcesUtils
import javax.inject.Inject

class ClassesViewModel @Inject constructor(
    private val classRepository: ClassRepository,
    semesterRepository: SemesterRepository
) {
    private var allClasses = listOf<ClassModel>()
    private val _classesUiState: MutableStateFlow<ClassesUiState>
    val classesUiState: StateFlow<ClassesUiState>

    init {
        val semesters = semesterRepository.getAllSemesters().map { semesterModel ->
            "${semesterModel.year}-${semesterModel.semesterNumber}"
        }

        _classesUiState = MutableStateFlow(
            ClassesUiState(
                classes = allClasses,
                isClassesLoading = true,
                departmentNames = listOf(),
                semesters = semesters,
                semesterFilter = semesters.first(),
                ratingFilters = ResourcesUtils.Strings.RATING_FILTERS
            )
        )

        classesUiState = _classesUiState.asStateFlow()

        loadAllClasses()
    }

    private fun loadAllClasses() {
        CoroutineScope(Dispatchers.IO).launch {
            allClasses = classRepository.getAllClasses()

            _classesUiState.update { classesUiState ->
                classesUiState.copy(
                    classes = filterClasses(null, null, classesUiState.semesters.first()),
                    departmentNames = allClasses.map { it.departmentName }.distinct(),
                    isClassesLoading = false
                )
            }
        }
    }

    fun updateSearchSubjectFilter(newSearchFilter: String?) {
        _classesUiState.update { classesUiState ->
            classesUiState.copy(
                searchSubjectFilter = newSearchFilter,
                classes = filterClasses(searchSubjectFilter = newSearchFilter)
            )
        }
    }

    fun updateDepartmentFilter(newDeptFilter: String?) {
        _classesUiState.update { classesUiState ->
            classesUiState.copy(
                departmentFilter = newDeptFilter,
                classes = filterClasses(deptFilter = newDeptFilter)
            )
        }
    }

    fun updateSemesterFilter(newSemesterFilter: String) {
        _classesUiState.update { classesUiState ->
            classesUiState.copy(
                semesterFilter = newSemesterFilter,
                classes = filterClasses(semesterFilter = newSemesterFilter)
            )
        }
    }

    fun updateRatingFilter(newRatingFilter: String?) {
        val ratingFilterNumber = newRatingFilter?.find { it.isDigit() }?.digitToInt()

        _classesUiState.update { classesUiState ->
            classesUiState.copy(
                currentRatingFilter = newRatingFilter,
                classes = filterClasses(ratingFilter = ratingFilterNumber)
            )
        }
    }

    private fun filterClasses(
        searchSubjectFilter: String? = _classesUiState.value.searchSubjectFilter,
        deptFilter: String? = _classesUiState.value.departmentFilter,
        semesterFilter: String = _classesUiState.value.semesterFilter,
        ratingFilter: Int? = _classesUiState.value.currentRatingFilter?.find { it.isDigit() }?.digitToInt()
    ): List<ClassModel> {
        return allClasses.filter { classModel ->
            val semesterYear = classModel.semester.year
            val semesterNumber = classModel.semester.semesterNumber
            val classSemester = "$semesterYear-$semesterNumber"
            val classScoreRounded = classModel.score?.toInt()

            (searchSubjectFilter == null || classModel.subjectName.contains(searchSubjectFilter, ignoreCase = true))
                    && (deptFilter == null || classModel.departmentName == deptFilter)
                    && (classSemester == semesterFilter)
                    && (ratingFilter == null ||
                        (classScoreRounded != null && classScoreRounded >= ratingFilter))
        }
    }

}