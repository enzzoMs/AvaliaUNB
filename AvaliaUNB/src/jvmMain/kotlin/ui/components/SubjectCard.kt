package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import data.models.SubjectModel
import resources.StringResources
import theme.White

@Composable
fun SubjectCard(
    subject: SubjectModel
) {
    Box(
        modifier = Modifier
            .padding(bottom = 14.dp)
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(White)
                .padding(14.dp)
        ) {
            // Subject name and code
            Row(
                modifier = Modifier
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = subject.name,
                    style = MaterialTheme.typography.subtitle1,
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
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                    )
                }

            }

            // Subject department name
            Row(
                modifier = Modifier
                    .padding(bottom = 6.dp)
            ) {
                Text(
                    text = StringResources.DEPARTMENT_FIELD_PREFIX,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier
                        .padding(end = 10.dp)
                )
                Text(
                    text = subject.departmentName,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1
                )
            }

            // Subject semester
            Row(
                modifier = Modifier
                    .padding(bottom = 6.dp)
            ) {
                Text(
                    text = StringResources.SEMESTER_FIELD_PREFIX,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier
                        .padding(end = 10.dp)
                )
                Text(
                    text = subject.semester,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Normal
                )
            }

            // Subject number of classes
            Row {
                Text(
                    text = StringResources.NUM_OF_CLASSES_FIELD_PREFIX,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier
                        .padding(end = 10.dp)
                )
                Text(
                    text = subject.numberOfClasses.toString(),
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}