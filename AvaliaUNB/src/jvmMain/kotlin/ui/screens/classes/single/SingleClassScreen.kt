package ui.screens.classes.single

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.Group
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.models.ClassReviewModel
import data.models.TeacherModel
import theme.*
import ui.components.buttons.SecondaryButton
import ui.components.cards.CardInformation
import ui.components.cards.ClassCard
import ui.components.review.ReviewForm
import ui.components.review.RatingInformation
import ui.components.review.ReviewList
import ui.components.schedule.ClassWeeklySchedule
import ui.screens.classes.single.viewmodel.SingleClassViewModel
import utils.resources.ResourcesUtils

@Composable
fun SingleClassScreen(
    singleClassViewModel: SingleClassViewModel,
    onBackClicked: () -> Unit
) {
    val singleClassUiState by singleClassViewModel.singleClassUiState.collectAsState()

    Box {
        val stateVertical = rememberScrollState()

        Column (
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

            TeacherInformation(singleClassUiState.teacherModel)

            Rating(
                score = singleClassUiState.classModel.score,
                classReviews = singleClassUiState.reviews
            )

            Reviews(
                reviewComment = singleClassUiState.reviewComment,
                onCommentChanged = { singleClassViewModel.updateReviewComment(it) },
                error = singleClassUiState.userAlreadyMadeReview,
                onPublishClicked = { comment, rating -> singleClassViewModel.publishReview(comment, rating) },
                classReviews = singleClassUiState.reviews,
                isLoading = singleClassUiState.isReviewsLoading
            )

            Spacer(
                modifier = Modifier
                    .weight(1f)
            )

            BackButton(
                onClicked = onBackClicked,
                modifier = Modifier
                    .padding(end = 14.dp, bottom = 24.dp)
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
                    onClick = { seeDetails = !seeDetails },
                    modifier = Modifier
                        .padding(end = 16.dp)
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

@Composable
private fun TeacherInformation(
    teacherModel: TeacherModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .padding(start = 14.dp, end = 14.dp, bottom = 14.dp, top = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .clip(RoundedCornerShape(percent = 15))
                    .background(UnbBlue)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Group,
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier
                        .padding(5.dp)
                )
            }
            Text(
                text = ResourcesUtils.Strings.TEACHER,
                style = MaterialTheme.typography.subtitle1,
                fontSize = 20.sp
            )
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(White)
                .then(modifier)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    bitmap = teacherModel.profilePicture,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(145.dp)
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    CardInformation(
                        fieldName = ResourcesUtils.Strings.NAME_FIELD_PREFIX,
                        fieldNameTextStyle = MaterialTheme.typography.subtitle1,
                        fieldText = teacherModel.name,
                        fieldTextStyle = MaterialTheme.typography.body1,
                        modifier = Modifier
                            .padding(bottom = 6.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = ResourcesUtils.Strings.SCORE_FIELD_PREFIX,
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier
                                .padding(end = 10.dp)
                        )
                        Icon(
                            painter = painterResource(ResourcesUtils.ImagePaths.GRADE),
                            contentDescription = null,
                            tint = LightGray,
                            modifier = Modifier
                                .size(35.dp)
                                .padding(end = 12.dp, bottom = 5.dp)
                        )
                        Text(
                            text = ResourcesUtils.Strings.NO_REVIEW,
                            style = TextStyle(
                                fontFamily = MaterialTheme.typography.subtitle2.fontFamily,
                                fontSize = 18.sp,
                                color = Gray
                            ),
                            fontWeight = FontWeight.Normal,
                            maxLines = 1
                        )
                    }

                    Row {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                        )
                        SecondaryButton(
                            label = ResourcesUtils.Strings.SEE_TEACHER_DETAILS,
                            onClick = {}
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Rating(
    score: Double?,
    classReviews: List<ClassReviewModel>
) {
    Column(
        modifier = Modifier
            .padding(start = 14.dp, end = 14.dp, top = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 30.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .clip(RoundedCornerShape(percent = 15))
                    .background(UnbBlue)
            ) {
                Icon(
                    imageVector = Icons.Filled.Grade,
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier
                        .padding(5.dp)
                )
            }
            Text(
                text = ResourcesUtils.Strings.RATINGS,
                style = MaterialTheme.typography.subtitle1,
                fontSize = 20.sp
            )
        }

        RatingInformation(
            score = score,
            reviews = classReviews
        )
    }
}

@Composable
private fun Reviews(
    reviewComment: String,
    onCommentChanged: (String) -> Unit,
    error: Boolean,
    onPublishClicked: (String, Int) -> Unit,
    classReviews: List<ClassReviewModel>,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 20.dp)
    ) {
        ReviewForm(
            value = reviewComment,
            onValueChanged = onCommentChanged,
            errorMessage = ResourcesUtils.Strings.FIELD_ERROR_REVIEW_ALREADY_MADE,
            error = error,
            onPublishClicked = onPublishClicked,
        )
        ReviewList(
            reviews = classReviews,
            isLoading = isLoading,
            modifier = Modifier
                .padding(top = 16.dp)
        )
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