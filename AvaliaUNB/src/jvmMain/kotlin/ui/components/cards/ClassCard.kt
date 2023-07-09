package ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.models.ClassModel
import theme.Gray
import theme.LightGray
import theme.White
import ui.components.cards.CardInformation
import utils.resources.ResourcesUtils

@Composable
fun ClassCard(
    classModel: ClassModel,
    backgroundColor: Color = White,
    rippleColor: Color = Gray,
    subjectTitleTextStyle: TextStyle = MaterialTheme.typography.subtitle1,
    fieldNameTextStyle: TextStyle = MaterialTheme.typography.subtitle1,
    fieldTextStyle: TextStyle = MaterialTheme.typography.body1,
    showScore: Boolean = true,
    clickable: Boolean = false,
    onClick: (ClassModel) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .padding(bottom = 14.dp)
            .clip(RoundedCornerShape(10.dp))
            .then(
                if (clickable) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(color = rippleColor)
                    )
                    { onClick(classModel) }
                } else Modifier
            )
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(backgroundColor)
                .padding(14.dp)
        ) {
            // Class name, subject code and number of hours
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = classModel.subjectName,
                    style = subjectTitleTextStyle,
                    fontWeight = FontWeight.Bold
                )
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(percent = 50))
                        .background(classModel.departmentColor)
                ) {
                    Text(
                        text = "Turma ${classModel.code}",
                        style = subjectTitleTextStyle,
                        modifier = Modifier
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                    )
                }

            }

            // Class department name
            CardInformation(
                fieldName = ResourcesUtils.Strings.DEPARTMENT_FIELD_PREFIX,
                fieldNameTextStyle = fieldNameTextStyle,
                fieldText = classModel.departmentName,
                fieldTextStyle = fieldTextStyle,
                modifier = Modifier
                    .padding(bottom = 6.dp)
            )

            // Class schedule
            CardInformation(
                fieldName = ResourcesUtils.Strings.SCHEDULE_FIELD_PREFIX,
                fieldNameTextStyle = fieldNameTextStyle,
                fieldText = classModel.schedule ?: ResourcesUtils.Strings.DEFAULT_CLASS_SCHEDULE,
                fieldTextStyle = fieldTextStyle,
                modifier = Modifier
                    .padding(bottom = 6.dp)
            )

            // Class location
            CardInformation(
                fieldName = ResourcesUtils.Strings.LOCATION_FIELD_PREFIX,
                fieldNameTextStyle = fieldNameTextStyle,
                fieldText = classModel.location,
                fieldTextStyle = fieldTextStyle,
                modifier = Modifier
                    .padding(bottom = 6.dp)
            )

            // Class teacher
            CardInformation(
                fieldName = ResourcesUtils.Strings.TEACHER_FIELD_PREFIX,
                fieldNameTextStyle = fieldNameTextStyle,
                fieldText = classModel.teacherName,
                fieldTextStyle = fieldTextStyle,
                modifier = Modifier
                    .padding(bottom = 6.dp)
            )

            // Class filled seats
            CardInformation(
                fieldName = ResourcesUtils.Strings.FILLED_SEATS_FIELD_PREFIX,
                fieldNameTextStyle = fieldNameTextStyle,
                fieldText = "${classModel.filledSeats}/${classModel.totalSeats}",
                fieldTextStyle = fieldTextStyle,
                modifier = Modifier
                    .padding(bottom = 6.dp)
            )

            // Class semester
            CardInformation(
                fieldName = ResourcesUtils.Strings.SEMESTER_FIELD_PREFIX,
                fieldNameTextStyle = fieldNameTextStyle,
                fieldText = "${classModel.semester.year}-${classModel.semester.semesterNumber}",
                fieldTextStyle = fieldTextStyle,
                modifier = Modifier
                    .padding(bottom = 6.dp)
            )
        }

        if (showScore) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(14.dp)
            ) {
                Icon(
                    painter = painterResource(ResourcesUtils.ImagePaths.GRADE),
                    contentDescription = null,
                    tint = LightGray,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(end = 12.dp, bottom = 5.dp)
                )
                if (classModel.score == null) {
                    Text(
                        text = ResourcesUtils.Strings.NO_REVIEW_MULTILINE,
                        style = TextStyle(
                            fontFamily = MaterialTheme.typography.subtitle2.fontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp,
                            color = Gray
                        )
                    )
                }
            }
        }

    }
}