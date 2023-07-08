package ui.screens.classes.single

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import theme.DarkAntiFlashWhite
import theme.DarkCharcoal
import theme.UnbBlue
import theme.White
import ui.components.ClassCard
import ui.components.ClassWeeklySchedule
import ui.components.SecondaryButton
import ui.screens.classes.single.viewmodel.SingleClassViewModel
import utils.resources.ResourcesUtils

@Composable
fun SingleClassScreen(
    singleClassViewModel: SingleClassViewModel
) {
    val singleClassUiState by singleClassViewModel.singleClassUiState.collectAsState()

    Box {
        val stateVertical = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkAntiFlashWhite)
                .padding(horizontal = 20.dp, vertical = 5.dp)
                .verticalScroll(stateVertical)
        ) {
            ClassCard(
                classModel = singleClassUiState.classModel,
                backgroundColor = DarkAntiFlashWhite,
                subjectTitleTextStyle = TextStyle(
                    fontFamily = MaterialTheme.typography.subtitle1.fontFamily,
                    fontWeight = MaterialTheme.typography.subtitle1.fontWeight,
                    fontSize = 20.sp,
                    color = DarkCharcoal
                ),
                showScore = false
            )

            ClassSchedule(
                schedule = singleClassUiState.classModel.schedule,
                classSubjectCode = singleClassUiState.classModel.subjectCode
            )
        }

        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(
                scrollState = stateVertical
            ),
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.TopEnd)
        )
    }
}

@Composable
private fun ClassSchedule(
    schedule: String?,
    classSubjectCode: String
) {
    var seeDetails by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(start = 14.dp, end = 14.dp, bottom = 14.dp)
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
                    imageVector = Icons.Outlined.Alarm,
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier
                        .padding(5.dp)
                )
            }

            Text(
                text = ResourcesUtils.Strings.SCHEDULE_FIELD_PREFIX,
                style = MaterialTheme.typography.subtitle1,
                fontSize = 20.sp
            )
            Text(
                text = schedule ?: ResourcesUtils.Strings.DEFAULT_CLASS_SCHEDULE,
                style = MaterialTheme.typography.body1,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(start = 10.dp)
            )

            if (schedule != null) {
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                )
                SecondaryButton(
                    label = ResourcesUtils.Strings.SEE_DETAILS,
                    onClick = { seeDetails = !seeDetails }
                )
            }
        }

        if (schedule != null) {
            AnimatedVisibility(seeDetails) {
                ClassWeeklySchedule(schedule, classSubjectCode)
            }
        }
    }
}