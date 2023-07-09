package ui.components.review

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import data.models.ReviewModel
import theme.*
import utils.resources.ResourcesUtils

@Composable
fun ReviewList(
    reviews: List<ReviewModel>,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    when {
        isLoading -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 80.dp)
            ) {
                CircularProgressIndicator(
                    color = LightGray,
                    strokeWidth = 3.dp,
                    modifier = Modifier
                        .size(80.dp)
                )
                Text(
                    text = ResourcesUtils.Strings.LOADING,
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier
                        .padding(top = 16.dp)
                )
            }
        }
        reviews.isEmpty() -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 80.dp)
            ) {
                Icon(
                    painter = painterResource(ResourcesUtils.ImagePaths.REVIEWS),
                    contentDescription = null,
                    tint = MediumGray,
                    modifier = Modifier
                        .size(80.dp)
                )
                Text(
                    text = ResourcesUtils.Strings.NO_REVIEW_PUBLISHED,
                    style = MaterialTheme.typography.subtitle2,
                    color = SilverChalice,
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
                        ReviewCard(review)
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