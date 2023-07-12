package ui.components.forms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import theme.White
import utils.resources.ResourcesUtils

@Composable
fun SearchAndFilterFields (
    searchName: String? = null,
    onSearchNameChanged: (String?) -> Unit = {},
    departmentsName: List<String> = listOf(),
    currentSelectedDepartmentFilter: String? = null,
    onSelectDepartmentFilter: (String?) -> Unit = {},
    semesters: List<String> = listOf(),
    currentSelectedSemesterFilter: String,
    onSelectSemesterFilter: (String) -> Unit = {},
    showRatingFilterField: Boolean = false,
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
                    value = searchName ?: "",
                    onValueChange = { newSearchFilter -> onSearchNameChanged(
                        if (newSearchFilter == "") null else newSearchFilter)
                    },
                    hintText = ResourcesUtils.Strings.SEARCH_FIELD_HINT,
                    backgroundColor = White,
                    startIcon = Icons.Filled.Search
                )
            }

            if (showRatingFilterField) {
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
