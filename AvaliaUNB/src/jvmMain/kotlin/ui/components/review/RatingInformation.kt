package ui.components.review

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.models.ReviewModel
import theme.DimGray
import theme.LightGray
import utils.resources.ResourcesUtils

private val SCORE_RANGE = 1..5

@Composable
fun RatingInformation(
    score: Int?,
    reviews: List<ReviewModel>,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(ResourcesUtils.ImagePaths.GRADE),
            contentDescription = null,
            tint = LightGray,
            modifier = Modifier
                .size(60.dp)
                .padding(end = 12.dp, bottom = 5.dp)
        )
        if (score == null) {
            Text(
                text = ResourcesUtils.Strings.NO_REVIEW_MULTILINE,
                style = TextStyle(
                    fontFamily = MaterialTheme.typography.subtitle2.fontFamily,
                    fontSize = 18.sp,
                    color = DimGray
                )
            )
        }

        ReviewsInformation(
            reviews = reviews,
            modifier = Modifier
                .weight(1f)
                .padding(start = 60.dp)
        )

        Spacer(
            modifier = Modifier
                .weight(1.4f)
            )
    }
}

@Composable
private fun ReviewsInformation(
    reviews: List<ReviewModel>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(end = 10.dp)
        ) {
            for (score in SCORE_RANGE) {
                Text(
                    text = "$score ⭐",
                    style = MaterialTheme.typography.subtitle2,
                    fontSize = 18.sp
                )
            }
        }

        Column {
            for (score in SCORE_RANGE) {
                val totalNumberOfReviews = reviews.size
                val scoreNumberOfReviews = reviews.filter { it.score == score }.size

                Row(
                   verticalAlignment = Alignment.CenterVertically
                ) {
                    LinearProgressIndicator(
                        progress = if (totalNumberOfReviews != 0) (scoreNumberOfReviews/totalNumberOfReviews).toFloat() else 0f,
                        backgroundColor = LightGray,
                        modifier = Modifier
                            .clip(CircleShape)
                            .weight(1f)
                            .padding(end = 10.dp)
                    )
                    Text(
                        text = scoreNumberOfReviews.toString(),
                        style = MaterialTheme.typography.subtitle2,
                        color = LightGray,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}