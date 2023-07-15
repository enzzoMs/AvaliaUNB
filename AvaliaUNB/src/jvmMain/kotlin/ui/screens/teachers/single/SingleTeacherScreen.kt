package ui.screens.teachers.single

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Grade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.models.TeacherReviewModel
import ui.components.buttons.SecondaryButton
import ui.components.cards.TeacherCard
import ui.components.review.RatingInformation
import ui.components.review.Reviews
import ui.screens.teachers.single.viewmodel.SingleTeacherViewModel
import utils.resources.Colors
import utils.resources.Strings

@Composable
fun SingleTeacherScreen(
    singleTeacherViewModel: SingleTeacherViewModel,
    onBackClicked: () -> Unit
) {
    val singleTeacherUiState by singleTeacherViewModel.singleTeacherUiState.collectAsState()

    Box {
        val stateVertical = rememberScrollState()

        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.DarkAntiFlashWhite)
                .padding(horizontal = 34.dp, vertical = 20.dp)
                .verticalScroll(stateVertical)
        ) {
            TeacherCard(
                teacherModel = singleTeacherUiState.teacherModel,
                backgroundColor = Colors.DarkAntiFlashWhite,
                showScore = false
            )

            Rating(
                score = singleTeacherUiState.teacherModel.score,
                teacherReviews = singleTeacherUiState.reviews
            )

            Reviews(
                reviewComment = singleTeacherUiState.reviewComment,
                onCommentChanged = { singleTeacherViewModel.updateReviewComment(it) },
                error = singleTeacherUiState.userAlreadyMadeReview,
                onPublishClicked = { comment, rating -> singleTeacherViewModel.publishReview(comment, rating) },
                reviews = singleTeacherUiState.reviews,
                isLoading = singleTeacherUiState.isReviewsLoading,
                decideShowEditButton = { reviewModel ->
                    singleTeacherViewModel.reviewBelongsToUser(reviewModel)
                },
                decideShowDeleteButton = { reviewModel ->
                    singleTeacherViewModel.let {
                        it.reviewBelongsToUser(reviewModel) || (it.userIsAdministrator() && it.reviewHasReports(reviewModel))
                    }
                },
                decideShowReportButton = { reviewModel ->
                    singleTeacherViewModel.let {
                        !it.reviewBelongsToUser(reviewModel) && !it.userIsAdministrator() && it.getUserReport(reviewModel) == null
                    }
                },
                decideShowReport = { report ->
                    singleTeacherViewModel.userIsAdministrator() || singleTeacherViewModel.reportBelongsToUser(report)
                },
                onEditClicked = { oldReviewModel, newRating, newComment ->
                    singleTeacherViewModel.editReview(oldReviewModel, newRating, newComment)
                },
                onRemoveClicked = { reviewModel ->
                    singleTeacherViewModel.deleteReview(reviewModel as TeacherReviewModel)
                },
                onReportClicked = { reviewId, description ->
                    singleTeacherViewModel.submitReviewReport(reviewId, description)
                },
                onEditReportClicked = { oldReportModel, newDescription ->
                    singleTeacherViewModel.editReport(oldReportModel, newDescription)
                },
                onRemoveReportClicked = { reportModel ->
                    singleTeacherViewModel.deleteReport(reportModel)
                },
                decideShowEditReport = { reportModel ->
                    singleTeacherViewModel.reportBelongsToUser(reportModel)
                },
                decideShowRemoveReport = { reportModel ->
                    singleTeacherViewModel.reportBelongsToUser(reportModel) || singleTeacherViewModel.userIsAdministrator()
                }
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
private fun Rating(
    score: Double?,
    teacherReviews: List<TeacherReviewModel>
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
            reviews = teacherReviews
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
