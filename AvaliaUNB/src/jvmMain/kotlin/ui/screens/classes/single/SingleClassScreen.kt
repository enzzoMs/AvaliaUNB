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
import data.models.ReportModel
import data.models.ReviewModel
import data.models.TeacherModel
import ui.components.buttons.SecondaryButton
import ui.components.cards.CardInformation
import ui.components.cards.ClassCard
import ui.components.review.RatingInformation
import ui.components.review.ReviewForm
import ui.components.review.ReviewList
import ui.components.schedule.ClassWeeklySchedule
import ui.screens.classes.single.viewmodel.SingleClassViewModel
import utils.resources.Colors
import utils.resources.Paths
import utils.resources.Strings

@Composable
fun SingleClassScreen(
    singleClassViewModel: SingleClassViewModel,
    onSeeTeacherDetailsClicked: (TeacherModel) -> Unit,
    onBackClicked: () -> Unit
) {
    val singleClassUiState by singleClassViewModel.singleClassUiState.collectAsState()

    Box {
        val stateVertical = rememberScrollState()

        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.DarkAntiFlashWhite)
                .padding(horizontal = 20.dp, vertical = 5.dp)
                .verticalScroll(stateVertical)
        ) {
            ClassCard(
                classModel = singleClassUiState.classModel,
                backgroundColor = Colors.DarkAntiFlashWhite,
                subjectTitleTextStyle = TextStyle(
                    fontFamily = MaterialTheme.typography.subtitle1.fontFamily,
                    fontWeight = MaterialTheme.typography.subtitle1.fontWeight,
                    fontSize = 20.sp,
                    color = Colors.DarkCharcoal
                ),
                showScore = false
            )

            ClassSchedule(
                schedule = singleClassUiState.classModel.schedule,
                classSubjectCode = singleClassUiState.classModel.subjectCode
            )

            TeacherInformation(
                teacherModel = singleClassUiState.teacherModel,
                onSeeTeacherDetailsClicked = onSeeTeacherDetailsClicked
            )

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
                isLoading = singleClassUiState.isReviewsLoading,
                decideShowEditRemoveButtons = { reviewModel ->
                    singleClassViewModel.reviewBelongsToUser(reviewModel)
                },
                decideShowReportButton = { reviewModel ->
                    !singleClassViewModel.reviewBelongsToUser(reviewModel) &&
                            !singleClassViewModel.userIsAdministrator()
                },
                getUserReport = { reviewModel ->
                    singleClassViewModel.getUserReport(reviewModel)
                },
                onRemoveClicked = { reviewModel ->
                    singleClassViewModel.deleteReview(reviewModel as ClassReviewModel)
                },
                onEditClicked = { oldReviewModel, newRating, newComment ->
                    singleClassViewModel.editReview(oldReviewModel, newRating, newComment)
                },
                onReportClicked = { reviewId, description ->
                    singleClassViewModel.submitReviewReport(reviewId, description)
                },
                onEditReportClicked = { reviewId, newDescription ->
                    singleClassViewModel.editReport(reviewId, newDescription)
                },
                onRemoveReportClicked = { reviewId ->
                    singleClassViewModel.deleteReport(reviewId)
                },
                onRemoveAnyReportClicked = { reportModel ->
                    singleClassViewModel.deleteReport(reportModel)
                },
                showAllReports = singleClassViewModel.userIsAdministrator(),
                getAllReports = { reviewModel -> singleClassViewModel.getReviewReports(reviewModel) },
                userRegistrationNumber = singleClassViewModel.getUserRegistrationNumber()
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
                    .background(Colors.UnbBlue)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Alarm,
                    contentDescription = null,
                    tint = Colors.White,
                    modifier = Modifier
                        .padding(5.dp)
                )
            }

            Text(
                text = Strings.FIELD_PREFIX_SCHEDULE,
                style = MaterialTheme.typography.subtitle1,
                fontSize = 20.sp
            )
            Text(
                text = schedule ?: Strings.DEFAULT_CLASS_SCHEDULE,
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
                    label = Strings.ACTION_SEE_DETAILS,
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
    onSeeTeacherDetailsClicked: (TeacherModel) -> Unit,
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
                    .background(Colors.UnbBlue)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Group,
                    contentDescription = null,
                    tint = Colors.White,
                    modifier = Modifier
                        .padding(5.dp)
                )
            }
            Text(
                text = Strings.TEACHER,
                style = MaterialTheme.typography.subtitle1,
                fontSize = 20.sp
            )
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Colors.White)
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
                        fieldName = Strings.FIELD_PREFIX_NAME,
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
                            text = Strings.FIELD_PREFIX_SCORE,
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier
                                .padding(end = 10.dp)
                        )
                        Icon(
                            painter = painterResource(Paths.Images.GRADE),
                            contentDescription = null,
                            tint = if (teacherModel.score == null) Colors.LightGray else Colors.AmericanOrange,
                            modifier = Modifier
                                .size(35.dp)
                                .padding(end = 12.dp, bottom = 5.dp)
                        )
                        if (teacherModel.score == null) {
                            Text(
                                text = Strings.NO_REVIEW,
                                style = TextStyle(
                                    fontFamily = MaterialTheme.typography.subtitle2.fontFamily,
                                    fontSize = 18.sp,
                                    color = Colors.Gray
                                ),
                                fontWeight = FontWeight.Normal,
                                maxLines = 1
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = String.format("%.1f", teacherModel.score),
                                    style = MaterialTheme.typography.h6
                                )
                                Text(
                                    text = " -  ${teacherModel.numOfReviews} análises",
                                    style = MaterialTheme.typography.subtitle2,
                                    fontSize = 18.sp,
                                    modifier = Modifier
                                        .padding(start = 6.dp)
                                )
                            }
                        }
                    }

                    Row {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                        )
                        SecondaryButton(
                            label = Strings.ACTION_SEE_DETAILS_TEACHER,
                            onClick = { onSeeTeacherDetailsClicked(teacherModel) }
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
                    .background(Colors.UnbBlue)
            ) {
                Icon(
                    imageVector = Icons.Filled.Grade,
                    contentDescription = null,
                    tint = Colors.White,
                    modifier = Modifier
                        .padding(5.dp)
                )
            }
            Text(
                text = Strings.RATINGS,
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
    onEditClicked: (ReviewModel, Int, String) -> Unit = { _: ReviewModel, _: Int, _: String -> },
    onRemoveClicked: (ReviewModel) -> Unit = {},
    onReportClicked: (Int, String) -> Unit = {_: Int, _: String -> },
    onEditReportClicked: (Int, String) -> Unit = {_: Int, _: String -> },
    onRemoveReportClicked: (Int) -> Unit = {_: Int ->},
    onRemoveAnyReportClicked: (ReportModel) -> Unit = {},
    onPublishClicked: (String, Int) -> Unit,
    decideShowEditRemoveButtons: (ReviewModel) -> Boolean,
    decideShowReportButton: (ReviewModel) -> Boolean,
    getUserReport: (ReviewModel) -> ReportModel?,
    classReviews: List<ClassReviewModel>,
    showAllReports: Boolean,
    getAllReports: (ReviewModel) -> List<ReportModel>,
    isLoading: Boolean,
    userRegistrationNumber: String,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 20.dp)
    ) {
        ReviewForm(
            value = reviewComment,
            onValueChanged = onCommentChanged,
            errorMessage = Strings.FIELD_ERROR_ALREADY_MADE_REVIEW,
            error = error,
            onPublishClicked = onPublishClicked,
        )
        ReviewList(
            reviews = classReviews,
            isLoading = isLoading,
            decideShowEditRemoveButtons = decideShowEditRemoveButtons,
            decideShowReportButton = decideShowReportButton,
            onRemoveClicked = onRemoveClicked,
            onEditClicked = onEditClicked,
            onReportClicked = onReportClicked,
            onEditReportClicked = onEditReportClicked,
            onRemoveUserReportClicked = onRemoveReportClicked,
            onRemoveAnyReportClicked = onRemoveAnyReportClicked,
            getUserReport = getUserReport,
            showAllReports = showAllReports,
            getAllReports = getAllReports,
            userRegistrationNumber = userRegistrationNumber,
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
            label = Strings.CAPITALIZED_BACK,
            onClick = onClicked,
            modifier = Modifier
                .weight(1f)
        )
    }
}