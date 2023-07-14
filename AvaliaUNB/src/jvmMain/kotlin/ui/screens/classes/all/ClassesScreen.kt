package ui.screens.classes.all

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
import data.models.ClassModel
import ui.components.cards.ClassCard
import ui.components.forms.SearchAndFilterFields
import ui.components.loading.Loading
import ui.screens.classes.all.viewmodel.ClassesViewModel
import utils.resources.Colors

@Composable
fun ClassesScreen(
    classesViewModel: ClassesViewModel,
    onClassClicked: (ClassModel) -> Unit = {}
) {
    val classesUiState by classesViewModel.classesUiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.DarkAntiFlashWhite)
            .padding(horizontal = 20.dp, vertical = 30.dp)
    ) {
        SearchAndFilterFields(
            searchName = classesUiState.searchSubjectFilter,
            onSearchNameChanged = { newSearchFilter -> classesViewModel.updateSearchSubjectFilter(newSearchFilter) },
            departmentsName = classesUiState.departmentNames,
            currentSelectedDepartmentFilter = classesUiState.departmentFilter,
            onSelectDepartmentFilter = { newFilter -> classesViewModel.updateDepartmentFilter(newFilter) },
            semesters = classesUiState.semesters,
            currentSelectedSemesterFilter = classesUiState.semesterFilter,
            onSelectSemesterFilter = { newFilter -> classesViewModel.updateSemesterFilter(newFilter) },
            ratingFilters = classesUiState.ratingFilters,
            currentSelectedRatingFilter = classesUiState.currentRatingFilter,
            onSelectRatingFilter = { newFilter -> classesViewModel.updateRatingFilter(newFilter) },
            showRatingFilterField = true
        )
        if (classesUiState.isClassesLoading) {
            Loading()
        } else {
            ClassesList(
                classes = classesUiState.classes,
                onClassClicked = onClassClicked,
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ClassesList(
    classes: List<ClassModel>,
    onClassClicked: (ClassModel) -> Unit = {},
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
            items(classes) { classModel ->
                ClassCard(
                    classModel = classModel,
                    clickable = true,
                    onClick = onClassClicked
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