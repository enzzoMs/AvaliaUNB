package ui.screens.classes.single.viewmodel

import data.models.ClassModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SingleClassViewModel(
    classModel: ClassModel
) {
    private val _singleClassUiState = MutableStateFlow(SingleClassUiState(classModel))
    val singleClassUiState = _singleClassUiState.asStateFlow()
}