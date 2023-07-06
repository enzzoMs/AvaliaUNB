package data.source.loading

data class DatabaseLoadingStatus(
    val schemaStatus: LoadingStatus = LoadingStatus.NOT_COMPLETED,
    val semestersLoadingStatus: List<SemesterLoadingStatus>,
    val finishedLoading: Boolean = false
)