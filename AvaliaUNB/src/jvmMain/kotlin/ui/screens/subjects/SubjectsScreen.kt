package ui.screens.subjects

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.models.SubjectModel
import resources.StringResources
import theme.DarkAntiFlashWhite
import theme.White
import ui.components.GeneralDropDownMenu
import ui.components.GeneralTextField
import ui.components.SubjectCard
import ui.screens.subjects.viewmodel.SubjectsViewModel

@Composable
fun SubjectsScreen(
    subjectsViewModel: SubjectsViewModel
) {
    val subjectUiState by subjectsViewModel.subjectUiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkAntiFlashWhite)
            .padding(horizontal = 20.dp, vertical = 30.dp)
    ) {
        SearchAndFilterFields(
            searchSubject = subjectUiState.searchSubjectFilter,
            onSearchSubjectChanged = { newSearchFilter -> subjectsViewModel.updateSearchSubjectFilter(newSearchFilter) },
            departmentsName = subjectUiState.departmentNames,
            currentSelectedDepartmentFilter = subjectUiState.departmentFilter,
            onSelectDepartmentFilter = { newFilter -> subjectsViewModel.updateDepartmentFilter(newFilter) },
            semesters = subjectUiState.semesters,
            currentSelectedSemesterFilter = subjectUiState.semesterFilter,
            onSelectSemesterFilter = { newFilter -> subjectsViewModel.updateSemesterFilter(newFilter) }
        )
        SubjectsLists(
            subjects = subjectUiState.subjects,
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun SearchAndFilterFields(
    searchSubject: String? = null,
    onSearchSubjectChanged: (String?) -> Unit = {},
    departmentsName: List<String> = listOf(),
    currentSelectedDepartmentFilter: String? = null,
    onSelectDepartmentFilter: (String?) -> Unit = {},
    semesters: List<String> = listOf(),
    currentSelectedSemesterFilter: String? = null,
    onSelectSemesterFilter: (String?) -> Unit = {}
) {
    Column {
        GeneralTextField(
            value = searchSubject ?: "",
            onValueChange = { newSearchFilter -> onSearchSubjectChanged(
                if (newSearchFilter == "") null else newSearchFilter)
            },
            hintText = StringResources.SEARCH_FIELD_HINT,
            backgroundColor = White,
            startIcon = Icons.Filled.Search,
            modifier = Modifier
                .fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            Text(
                text = StringResources.DEPARTMENT_FIELD_PREFIX,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .padding(end = 12.dp)
            )

            GeneralDropDownMenu(
                menuItems = listOf(StringResources.GENERAL_TEXT_ALL) + departmentsName,
                selectedItem = currentSelectedDepartmentFilter ?: StringResources.GENERAL_TEXT_ALL,
                onSelectItem = { newFilter -> onSelectDepartmentFilter(
                    if (newFilter == StringResources.GENERAL_TEXT_ALL) null else newFilter)
                },
                dropDownMenuMinWidth = 600.dp,
                dropDownMenuMinHeight = 400.dp,
                modifier = Modifier
                    .weight(3f)
            )

            Text(
                text = StringResources.SEMESTER_FIELD_PREFIX,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .padding(end = 12.dp, start = 16.dp)
            )
            GeneralDropDownMenu(
                menuItems = listOf(StringResources.GENERAL_TEXT_ALL) + semesters,
                selectedItem = currentSelectedSemesterFilter ?: StringResources.GENERAL_TEXT_ALL,
                onSelectItem = {newFilter -> onSelectSemesterFilter(
                    if (newFilter == StringResources.GENERAL_TEXT_ALL) null else newFilter)
                },
                dropDownMenuMinWidth = 100.dp,
                dropDownMenuMinHeight = 200.dp,
                modifier = Modifier
                    .weight(1f)
            )
        }

    }
}

@Composable
private fun SubjectsLists(
    subjects: List<SubjectModel>,
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
                SubjectCard(subject)
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