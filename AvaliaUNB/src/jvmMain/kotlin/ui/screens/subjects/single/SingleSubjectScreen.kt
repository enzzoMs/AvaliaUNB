package ui.screens.subjects.single

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.models.ClassModel
import theme.DarkAntiFlashWhite
import theme.DarkCharcoal
import theme.UnbBlue
import theme.White
import ui.components.ClassCard
import ui.components.SecondaryButton
import ui.components.SubjectCard
import ui.screens.subjects.single.viewmodel.SingleSubjectViewModel
import utils.resources.ResourcesUtils

@Composable
fun SingleSubjectScreen(
    singleSubjectViewModel: SingleSubjectViewModel,
    onBackClicked: () -> Unit = {},
    onClassClicked: (ClassModel) -> Unit = {}
) {
    val singleSubjectUiState by singleSubjectViewModel.singleSubjectUiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkAntiFlashWhite)
            .padding(horizontal = 20.dp, vertical = 5.dp)
    ) {
        SubjectCard(
            subject = singleSubjectUiState.subjectModel,
            backgroundColor = DarkAntiFlashWhite,
            subjectTitleTextStyle = TextStyle(
                fontFamily = MaterialTheme.typography.subtitle1.fontFamily,
                fontWeight = MaterialTheme.typography.subtitle1.fontWeight,
                fontSize = 20.sp,
                color = DarkCharcoal
            ),
        )
        SubjectClasses(
            classes = singleSubjectUiState.subjectClasses,
            modifier = Modifier.weight(1f),
            onClassClicked = onClassClicked
        )
        BackButton(
            onClicked = onBackClicked,
            modifier = Modifier
                .padding(end = 14.dp, bottom = 24.dp)
        )
    }
}

@Composable
private fun SubjectClasses(
    classes: List<ClassModel>,
    onClassClicked: (ClassModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .padding(start = 14.dp, end = 14.dp, bottom = 14.dp)
            .then(modifier)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .clip(RoundedCornerShape(percent = 15))
                    .background(UnbBlue)
            ) {
                Icon(
                    imageVector = Icons.Outlined.CollectionsBookmark,
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier
                        .padding(5.dp)
                )
            }

            Text(
                text = ResourcesUtils.Strings.CLASSES,
                style = MaterialTheme.typography.subtitle1,
                fontSize = 20.sp
            )
        }

        Box(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        ) {
            val listState = rememberLazyListState()

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .padding(end = 10.dp)
            ) {
                items(classes) { subjectClass ->
                    ClassCard(
                        classModel = subjectClass,
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

}

@Composable
private fun BackButton(
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        Spacer(
            modifier = Modifier
                .weight(1.5f)
        )
        SecondaryButton(
            label = ResourcesUtils.Strings.BACK_BUTTON,
            onClick = onClicked,
            modifier = Modifier
                .weight(1f)
        )
    }
}