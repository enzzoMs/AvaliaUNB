package ui.screens.subjects.single.viewmodel

import data.models.SubjectModel
import data.repositories.SubjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SingleSubjectViewModel(
    subjectModel: SubjectModel,
    subjectRepository: SubjectRepository
) {
    private val _singleSubjectUiState = MutableStateFlow(SingleSubjectUiState(
        subjectModel = subjectModel,
        subjectClasses = subjectRepository.getSubjectClasses(subjectModel)
    ))
    val singleSubjectUiState = _singleSubjectUiState.asStateFlow()

}