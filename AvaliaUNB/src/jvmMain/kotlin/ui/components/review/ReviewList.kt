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

@Composable
fun ReviewList(
    reviews: List<ReviewModel>,
    isLoading: Boolean = false,
    onEditClicked: (ReviewModel, Int, String) -> Unit = { _: ReviewModel, _: Int, _: String -> },
    onRemoveClicked: (ReviewModel) -> Unit = {},
    onReportClicked: (Int, String) -> Unit = {_: Int, _: String -> },
    onEditReportClicked: (Int, String) -> Unit = {_: Int, _: String -> },
    onRemoveUserReportClicked: (Int) -> Unit = { _: Int ->},
    onRemoveAnyReportClicked: (ReportModel) -> Unit = {},
    decideShowEditRemoveButtons: (ReviewModel) -> Boolean,
    decideShowReportButton: (ReviewModel) -> Boolean,
    getUserReport: (ReviewModel) -> ReportModel?,
    showAllReports: Boolean = false,
    getAllReports: (ReviewModel) -> List<ReportModel>,
    userRegistrationNumber: String,
    modifier: Modifier = Modifier
) {
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
                    .height(600.dp)
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
                            showEditAndRemove = decideShowEditRemoveButtons(review),
                            showReportButton = decideShowReportButton(review),
                            onEditClicked = onEditClicked,
                            onRemoveClicked = onRemoveClicked,
                            onReportClicked = onReportClicked,
                            onEditReportClicked = onEditReportClicked,
                            onRemoveUserReportClicked = onRemoveUserReportClicked,
                            onRemoveAnyReportClicked = onRemoveAnyReportClicked,
                            showAllReports = showAllReports,
                            getAllReports = getAllReports,
                            userReport = getUserReport(review),
                            userRegistrationNumber = userRegistrationNumber
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