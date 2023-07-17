package ui.screens.reports

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.models.ReportModel
import data.models.ReviewModel
import ui.components.loading.Loading
import ui.components.review.ReviewCard
import ui.screens.reports.viewmodel.ReportsViewModel
import utils.resources.Colors
import utils.resources.Strings

@Composable
fun ReportsScreen(
    reportsViewModel: ReportsViewModel,
    onUserClicked: (String) -> Unit
) {
    val reportsUiState by reportsViewModel.reportsUiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.DarkAntiFlashWhite)
            .padding(horizontal = 20.dp, vertical = 30.dp)
    ) {
        if (reportsUiState.isReviewsLoading) {
            Loading()
        } else {
            ReviewsList(
                reviews = reportsUiState.reviews,
                onRemoveReview = { reviewModel ->  reportsViewModel.deleteReview(reviewModel) },
                onRemoveReport = { reportModel ->  reportsViewModel.deleteReport(reportModel) },
                onUserClicked = onUserClicked,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ReviewsList(
    reviews: List<ReviewModel>,
    onRemoveReview: (ReviewModel) -> Unit,
    onRemoveReport: (ReportModel) -> Unit,
    onUserClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (reviews.isEmpty()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Outlined.Feedback,
                contentDescription = null,
                tint = Colors.MediumGray,
                modifier = Modifier
                    .size(80.dp)
            )
            Text(
                text = Strings.NO_REPORTS,
                style = MaterialTheme.typography.subtitle2,
                color = Colors.SilverChalice,
                modifier = Modifier
                    .padding(top = 16.dp)
            )
        }
    } else {
        Box (
            modifier = modifier
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
                        showDeleteButton = true,
                        decideShowReport = { true },
                        userNameClickable = true,
                        onUserClicked = onUserClicked,
                        onEditClicked = { _, _, _ -> },
                        onRemoveClicked = onRemoveReview,
                        onReportClicked = { _, _ ->},
                        onEditReportClicked = { _, _ ->},
                        onRemoveReportClicked = onRemoveReport,
                        decideShowEditReport = { false },
                        decideShowRemoveReport = { true },
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
            )
        }
    }
}