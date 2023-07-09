package ui.screens.classes.single.viewmodel

import data.models.ClassModel
import data.repositories.ClassRepository
import data.source.ClassDAO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SingleClassViewModel(
    classModel: ClassModel,
    classRepository: ClassRepository
) {
    private val _singleClassUiState = MutableStateFlow(
        SingleClassUiState(
            classModel = classModel,
            classRepository.getClassTeacher(classModel),
            classRepository.getClassReviews(classModel)
        )
    )

    val singleClassUiState = _singleClassUiState.asStateFlow()
}