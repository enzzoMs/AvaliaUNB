package ui.screens.splash.viewmodel

import data.source.loading.DatabaseLoadingStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import utils.database.DatabaseConfiguration
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class SplashViewModel @Inject constructor(
    @Named("databaseConfiguration") databaseConfiguration: DatabaseConfiguration,
    @Named("databaseLoadingStatus") databaseLoadingStatus: StateFlow<DatabaseLoadingStatus>
) {
    private val _splashUiState = MutableStateFlow(
            SplashUiState(
                reloadDatabase = databaseConfiguration.reloadDatabase,
                initializeData = databaseConfiguration.run {
                    loadDataForSemester2022_1 || loadDataForSemester2022_2 || loadDataForSemester2023_1
                },
                databaseLoadingStatus = databaseLoadingStatus
            )
        )
    val splashUiState = _splashUiState.asStateFlow()
}