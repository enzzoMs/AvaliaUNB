package data.source.loading

import utils.database.PrePopulatedSemester

data class SemesterLoadingStatus(
    val prePopulatedSemester: PrePopulatedSemester,
    val subjectsStatus: LoadingStatus = LoadingStatus.NOT_COMPLETED,
    val classesStatus: LoadingStatus = LoadingStatus.NOT_COMPLETED,
    val departmentsStatus: LoadingStatus = LoadingStatus.NOT_COMPLETED
)