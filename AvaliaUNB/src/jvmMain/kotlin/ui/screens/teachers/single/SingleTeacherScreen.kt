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
import data.models.ReportModel
import data.models.ReviewModel
import data.models.TeacherReviewModel
import theme.DarkAntiFlashWhite
import theme.UnbBlue
import theme.White
import ui.components.buttons.SecondaryButton
import ui.components.cards.TeacherCard
import ui.components.review.RatingInformation
import ui.components.review.ReviewForm
import ui.components.review.ReviewList
import ui.screens.teachers.single.viewmodel.SingleTeacherViewModel
import utils.resources.ResourcesUtils

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
                .background(DarkAntiFlashWhite)
                .padding(horizontal = 34.dp, vertical = 20.dp)
                .verticalScroll(stateVertical)
        ) {
            TeacherCard(
                teacherModel = singleTeacherUiState.teacherModel,
                backgroundColor = DarkAntiFlashWhite,
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
                teacherReviews = singleTeacherUiState.reviews,
                isLoading = singleTeacherUiState.isReviewsLoading,
                decideShowEditRemoveButtons = { reviewModel ->
                    singleTeacherViewModel.reviewBelongsToUser(reviewModel)
                },
                decideShowReportButton = { reviewModel ->
                    !singleTeacherViewModel.reviewBelongsToUser(reviewModel) &&
                            singleTeacherViewModel.userIsNotAdministrator()
                },
                getUserReport = { reviewModel ->
                    singleTeacherViewModel.getUserReport(reviewModel)
                },
                onRemoveClicked = { reviewModel ->
                    singleTeacherViewModel.deleteReview(reviewModel as TeacherReviewModel)
                },
                onEditClicked = { oldReviewModel, newRating, newComment ->
                    singleTeacherViewModel.editReview(oldReviewModel, newRating, newComment)
                },
                onReportClicked = { reviewId, description ->
                    singleTeacherViewModel.submitReviewReport(reviewId, description)
                },
                onEditReportClicked = { reviewId, newDescription ->
                    singleTeacherViewModel.editReport(reviewId, newDescription)
                },
                onRemoveReportClicked = { reviewId ->
                    singleTeacherViewModel.deleteReport(reviewId)
                },
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
            reviews = teacherReviews
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
    onPublishClicked: (String, Int) -> Unit,
    decideShowEditRemoveButtons: (ReviewModel) -> Boolean,
    decideShowReportButton: (ReviewModel) -> Boolean,
    getUserReport: (ReviewModel) -> ReportModel?,
    teacherReviews: List<TeacherReviewModel>,
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
            reviews = teacherReviews,
            isLoading = isLoading,
            decideShowEditRemoveButtons = decideShowEditRemoveButtons,
            decideShowReportButton = decideShowReportButton,
            onRemoveClicked = onRemoveClicked,
            onEditClicked = onEditClicked,
            onReportClicked = onReportClicked,
            onEditReportClicked = onEditReportClicked,
            onRemoveReportClicked = onRemoveReportClicked,
            getUserReport = getUserReport,
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
