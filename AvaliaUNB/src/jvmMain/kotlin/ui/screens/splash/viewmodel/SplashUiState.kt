package ui.screens.splash.viewmodel

import data.source.loading.DatabaseLoadingStatus
import data.source.loading.SemesterLoadingStatus
import kotlinx.coroutines.flow.StateFlow

data class SplashUiState(
    val reloadDatabase: Boolean = false,
    val initializeData: Boolean = false,
    val semestersLoadingStatus: List<SemesterLoadingStatus> = listOf(),
    val databaseLoadingStatus: StateFlow<DatabaseLoadingStatus>
)
