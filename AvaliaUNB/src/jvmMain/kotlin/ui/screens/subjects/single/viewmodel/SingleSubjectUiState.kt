package ui.screens.subjects.single.viewmodel

import data.models.ClassModel
import data.models.SubjectModel

data class SingleSubjectUiState(
    val subjectModel: SubjectModel,
    val subjectClasses: List<ClassModel>
)
