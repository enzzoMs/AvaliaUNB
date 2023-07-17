package ui.components.review

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import data.models.ReportModel
import data.models.ReviewModel
import ui.components.loading.Loading
import utils.resources.Colors
import utils.resources.Paths
import utils.resources.Strings

private val REVIEW_LIST_HEIGHT = 600.dp


@Composable
fun Reviews(
    reviewComment: String,
    onCommentChanged: (String) -> Unit,
    error: Boolean,
    onPublishClicked: (String, Int) -> Unit,
    reviews: List<ReviewModel>,
    isLoading: Boolean = false,
    decideShowEditButton: (ReviewModel) -> Boolean,
    decideShowDeleteButton: (ReviewModel) -> Boolean,
    decideShowReportButton: (ReviewModel) -> Boolean,
    decideShowReport: (ReportModel) -> Boolean,
    onUserClicked: (String) -> Unit,
    onEditClicked: (ReviewModel, Int, String) -> Unit,
    onRemoveClicked: (ReviewModel) -> Unit,
    onReportClicked: (Int, String) -> Unit,
    onEditReportClicked: (ReportModel, String) -> Unit,
    onRemoveReportClicked: (ReportModel) -> Unit,
    decideShowEditReport: (ReportModel) -> Boolean,
    decideShowRemoveReport: (ReportModel) -> Boolean,
    userNameClickable: Boolean,
    modifier: Modifier = Modifier
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

        // Review List --------------------------

        when {
            isLoading -> {
                Loading()
            }
            reviews.isEmpty() -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 80.dp)
                ) {
                    Icon(
                        painter = painterResource(Paths.Images.REVIEWS),
                        contentDescription = null,
                        tint = Colors.MediumGray,
                        modifier = Modifier
                            .size(80.dp)
                    )
                    Text(
                        text = Strings.NO_REVIEW_PUBLISHED,
                        style = MaterialTheme.typography.subtitle2,
                        color = Colors.SilverChalice,
                        modifier = Modifier
                            .padding(top = 16.dp)
                    )
                }
            }
            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(REVIEW_LIST_HEIGHT)
                        .then(modifier)
                ) {
                    val listState = rememberLazyListState()

                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .padding(end = 10.dp)
                    ) {

                        items(reviews) { review ->
                            ReviewCard(
                                review = review,
                                showEditButton = decideShowEditButton(review),
                                showDeleteButton = decideShowDeleteButton(review),
                                showReportButton = decideShowReportButton(review),
                                decideShowReport = decideShowReport,
                                userNameClickable = userNameClickable,
                                onUserClicked = onUserClicked,
                                onEditClicked = onEditClicked,
                                onRemoveClicked = onRemoveClicked,
                                onReportClicked = onReportClicked,
                                onEditReportClicked = onEditReportClicked,
                                onRemoveReportClicked = onRemoveReportClicked,
                                decideShowEditReport = decideShowEditReport,
                                decideShowRemoveReport = decideShowRemoveReport,
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
                            .padding(top = 18.dp)
                    )
                }
            }
        }
    }
}