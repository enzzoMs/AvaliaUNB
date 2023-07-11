package ui.screens.teachers.all

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
import data.models.TeacherModel
import theme.DarkAntiFlashWhite
import theme.White
import ui.components.cards.TeacherCard
import ui.components.forms.GeneralDropDownMenu
import ui.components.forms.GeneralTextField
import ui.components.loading.Loading
import ui.screens.teachers.all.viewmodel.TeachersViewModel
import utils.resources.ResourcesUtils

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
            searchTeacher = teachersUiState.searchTeacherNameFilter,
            onSearchTeacherChanged = { newSearchFilter -> teachersViewModel.updateSearchTeacherNameFilter(newSearchFilter) },
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
private fun SearchAndFilterFields(
    searchTeacher: String? = null,
    onSearchTeacherChanged: (String?) -> Unit = {},
    departmentsName: List<String> = listOf(),
    currentSelectedDepartmentFilter: String? = null,
    onSelectDepartmentFilter: (String?) -> Unit = {},
    semesters: List<String> = listOf(),
    currentSelectedSemesterFilter: String,
    onSelectSemesterFilter: (String) -> Unit = {},
    ratingFilters: List<String> = listOf(),
    currentSelectedRatingFilter: String? = null,
    onSelectRatingFilter: (String?) -> Unit = {}
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box (
                modifier = Modifier
                    .weight(3.6f)
            ) {
                GeneralTextField(
                    value = searchTeacher ?: "",
                    onValueChange = { newSearchFilter -> onSearchTeacherChanged(
                        if (newSearchFilter == "") null else newSearchFilter)
                    },
                    hintText = ResourcesUtils.Strings.SEARCH_FIELD_HINT,
                    backgroundColor = White,
                    startIcon = Icons.Filled.Search
                )
            }

            Text(
                text = ResourcesUtils.Strings.SCORE_FIELD_PREFIX,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .padding(end = 12.dp, start = 16.dp)
            )
            GeneralDropDownMenu(
                menuItems = listOf(ResourcesUtils.Strings.GENERAL_TEXT_ALL) + ratingFilters,
                selectedItem = currentSelectedRatingFilter ?: ResourcesUtils.Strings.GENERAL_TEXT_ALL,
                onSelectItem = { newFilter -> onSelectRatingFilter(
                    if (newFilter == ResourcesUtils.Strings.GENERAL_TEXT_ALL) null else newFilter
                ) },
                dropDownMenuMinWidth = 100.dp,
                dropDownMenuMinHeight = 200.dp,
                modifier = Modifier
                    .weight(1f)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            Text(
                text = ResourcesUtils.Strings.DEPARTMENT_FIELD_PREFIX,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .padding(end = 12.dp)
            )

            GeneralDropDownMenu(
                menuItems = listOf(ResourcesUtils.Strings.GENERAL_TEXT_ALL) + departmentsName,
                selectedItem = currentSelectedDepartmentFilter ?: ResourcesUtils.Strings.GENERAL_TEXT_ALL,
                onSelectItem = { newFilter -> onSelectDepartmentFilter(
                    if (newFilter == ResourcesUtils.Strings.GENERAL_TEXT_ALL) null else newFilter)
                },
                dropDownMenuMinWidth = 600.dp,
                dropDownMenuMinHeight = 400.dp,
                modifier = Modifier
                    .weight(3f)
            )

            Text(
                text = ResourcesUtils.Strings.SEMESTER_FIELD_PREFIX,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .padding(end = 12.dp, start = 16.dp)
            )
            GeneralDropDownMenu(
                menuItems = semesters,
                selectedItem = currentSelectedSemesterFilter,
                onSelectItem = { newFilter -> onSelectSemesterFilter(newFilter) },
                dropDownMenuMinWidth = 100.dp,
                dropDownMenuMinHeight = 200.dp,
                modifier = Modifier
                    .weight(1f)
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