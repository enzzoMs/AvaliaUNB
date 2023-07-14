package ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import data.models.SubjectModel
import utils.resources.Colors
import utils.resources.Strings

@Composable
fun SubjectCard(
    subject: SubjectModel,
    backgroundColor: Color = Colors.White,
    rippleColor: Color = Colors.Gray,
    subjectTitleTextStyle: TextStyle = MaterialTheme.typography.subtitle1,
    fieldNameTextStyle: TextStyle = MaterialTheme.typography.subtitle1,
    fieldTextStyle: TextStyle = MaterialTheme.typography.body1,
    clickable: Boolean = false,
    onClick: (SubjectModel) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .padding(bottom = 14.dp)
            .clip(RoundedCornerShape(10.dp))
            .then(
                if (clickable) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(color = rippleColor))
                        { onClick(subject) }
                } else Modifier
            )
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(backgroundColor)
                .padding(14.dp)
        ) {
            // Subject name and code
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = subject.name,
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
                        .background(subject.departmentColor)
                ) {
                    Text(
                        text = subject.code,
                        style = subjectTitleTextStyle,
                        modifier = Modifier
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                    )
                }

            }

            // Subject department name
            CardInformation(
                fieldName = Strings.FIELD_PREFIX_DEPARTMENT,
                fieldNameTextStyle = fieldNameTextStyle,
                fieldText = subject.departmentName,
                fieldTextStyle = fieldTextStyle,
                modifier = Modifier
                    .padding(bottom = 6.dp)
            )

            // Subject semester
            CardInformation(
                fieldName = Strings.FIELD_PREFIX_SEMESTER,
                fieldNameTextStyle = fieldNameTextStyle,
                fieldText = subject.semester,
                fieldTextStyle = fieldTextStyle,
                modifier = Modifier
                    .padding(bottom = 6.dp)
            )

            // Subject number of classes
            CardInformation(
                fieldName = Strings.FIELD_PREFIX_NUM_OF_CLASSES,
                fieldNameTextStyle = fieldNameTextStyle,
                fieldText = subject.numberOfClasses.toString(),
                fieldTextStyle = fieldTextStyle
            )
        }
    }
}