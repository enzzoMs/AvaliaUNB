package ui.screens.teachers.all

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.models.TeacherModel
import theme.DarkAntiFlashWhite
import ui.components.cards.TeacherCard
import ui.components.forms.SearchAndFilterFields
import ui.components.loading.Loading
import ui.screens.teachers.all.viewmodel.TeachersViewModel

@Composable
fun TeachersScreen(
    teachersViewModel: TeachersViewModel,
    onTeacherClicked: (TeacherModel) -> Unit = {}
) {
    val teachersUiState by teachersViewModel.teachersUiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkAntiFlashWhite)
            .padding(horizontal = 20.dp, vertical = 30.dp)
    ) {
        SearchAndFilterFields(
            searchName = teachersUiState.searchTeacherNameFilter,
            onSearchNameChanged = { newSearchFilter -> teachersViewModel.updateSearchTeacherNameFilter(newSearchFilter) },
            departmentsName = teachersUiState.departmentNames,
            currentSelectedDepartmentFilter = teachersUiState.departmentFilter,
            onSelectDepartmentFilter = { newFilter -> teachersViewModel.updateDepartmentFilter(newFilter) },
            semesters = teachersUiState.semesters,
            currentSelectedSemesterFilter = teachersUiState.semesterFilter,
            onSelectSemesterFilter = { newFilter -> teachersViewModel.updateSemesterFilter(newFilter) },
            ratingFilters = teachersUiState.ratingFilters,
            currentSelectedRatingFilter = teachersUiState.currentRatingFilter,
            onSelectRatingFilter = { newFilter -> teachersViewModel.updateRatingFilter(newFilter) }
        )
        if (teachersUiState.isTeachersLoading) {
            Loading()
        } else {
            TeachersList(
                teachers = teachersUiState.teachers,
                onTeacherClicked = onTeacherClicked,
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth()
            )
        }
    }
}


@Composable
private fun TeachersList(
    teachers: List<TeacherModel>,
    onTeacherClicked: (TeacherModel) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box (
        modifier = modifier
    ) {
        val listState = rememberLazyListState()

        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(end = 10.dp)
        ) {
            items(teachers) { teacherModel ->
                TeacherCard(
                    teacherModel = teacherModel,
                    clickable = true,
                    onClick = onTeacherClicked
                )
            }
        }

        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(
                scrollState = listState
            ),
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.TopEnd)
        )
    }
}