package ui.components.review

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import utils.resources.Colors
import utils.resources.Paths
import utils.resources.Strings

private val SCORE_RANGE = 5 downTo 1

@Composable
fun RatingInformation(
    score: Double?,
    reviews: List<ReviewModel>,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(Paths.Images.GRADE),
            contentDescription = null,
            tint = if (score == null) Colors.LightGray else Colors.AmericanOrange,
            modifier = Modifier
                .size(60.dp)
                .padding(end = 12.dp, bottom = 5.dp)
        )
        if (score == null) {
            Text(
                text = Strings.NO_REVIEW_MULTILINE,
                style = TextStyle(
                    fontFamily = MaterialTheme.typography.subtitle2.fontFamily,
                    fontSize = 18.sp,
                    color = Colors.DimGray
                )
            )
        } else {
            Column {
                Text(
                    text = String.format("%.1f", score),
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                )
                Text(
                    text = "${reviews.size} análises",
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                )
            }
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
                    fontSize = 18.sp,
                    color = if (reviews.isEmpty()) Colors.Gray else Colors.AmericanOrange
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(end = 10.dp)
                .weight(1f)
        ) {
            for (score in SCORE_RANGE) {
                val totalNumberOfReviews = reviews.size
                val scoreNumberOfReviews = reviews.filter { it.rating == score }.size

                LinearProgressIndicator(
                    progress = if (totalNumberOfReviews != 0) {
                        (scoreNumberOfReviews.toFloat()/totalNumberOfReviews.toFloat())
                    } else {
                        0f
                    },
                    backgroundColor = Colors.LightGray,
                    color = Colors.AmericanOrange,
                    modifier = Modifier
                        .clip(RoundedCornerShape(percent = 50))
                        .padding(vertical = 10.dp)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(end = 10.dp)
        ) {
            for (score in SCORE_RANGE) {
                val scoreNumberOfReviews = reviews.filter { it.rating == score }.size

                Text(
                    text = scoreNumberOfReviews.toString(),
                    style = MaterialTheme.typography.subtitle2,
                    color = Colors.Silver,
                    fontSize = 18.sp
                )
            }
        }
    }
}