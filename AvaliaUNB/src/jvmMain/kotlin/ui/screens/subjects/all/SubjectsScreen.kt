package ui.screens.subjects.all

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
import data.models.SubjectModel
import ui.components.cards.SubjectCard
import ui.components.forms.SearchAndFilterFields
import ui.components.loading.Loading
import ui.screens.subjects.all.viewmodel.SubjectsViewModel
import utils.resources.Colors

@Composable
fun SubjectsScreen(
    subjectsViewModel: SubjectsViewModel,
    onSubjectClicked: (SubjectModel) -> Unit = {}
) {
    val subjectUiState by subjectsViewModel.subjectUiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.DarkAntiFlashWhite)
            .padding(horizontal = 20.dp, vertical = 30.dp)
    ) {
        SearchAndFilterFields(
            searchName = subjectUiState.searchSubjectFilter,
            onSearchNameChanged = { newSearchFilter -> subjectsViewModel.updateSearchSubjectFilter(newSearchFilter) },
            departmentsName = subjectUiState.departmentNames,
            currentSelectedDepartmentFilter = subjectUiState.departmentFilter,
            onSelectDepartmentFilter = { newFilter -> subjectsViewModel.updateDepartmentFilter(newFilter) },
            semesters = subjectUiState.semesters,
            currentSelectedSemesterFilter = subjectUiState.semesterFilter,
            onSelectSemesterFilter = { newFilter -> subjectsViewModel.updateSemesterFilter(newFilter) }
        )
        if (subjectUiState.isSubjectsLoading) {
            Loading()
        } else {
            SubjectsList(
                subjects = subjectUiState.subjects,
                onSubjectClicked = onSubjectClicked,
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth()
            )
        }
    }
}


@Composable
private fun SubjectsList(
    subjects: List<SubjectModel>,
    onSubjectClicked: (SubjectModel) -> Unit = {},
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
            items(subjects) { subject ->
                SubjectCard(
                    subject = subject,
                    clickable = true,
                    onClick = onSubjectClicked
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