package ui.components.review

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import data.models.ReviewModel
import theme.AmericanOrange
import theme.Gray
import theme.White
import utils.resources.ResourcesUtils

const val MAX_SCORE_STARS = 5

@Composable
fun ReviewCard(
    review: ReviewModel,
    userNameTextStyle: TextStyle = MaterialTheme.typography.body2,
    backgroundColor: Color = White,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)
            .background(backgroundColor)
            .then(modifier)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 12.dp)
            ) {
                Image(
                    bitmap = review.userProfilePicture,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 10.dp)
                )
                Text(
                    text = review.userName,
                    style = userNameTextStyle,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(end = 10.dp)
                )
                Text(
                    text = ResourcesUtils.Strings.PUBLISHED,
                    style = userNameTextStyle
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 12.dp)
            ) {
                repeat(review.rating) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = AmericanOrange,
                        modifier = Modifier
                            .size(16.dp)
                    )
                }
                repeat(MAX_SCORE_STARS - review.rating) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Gray,
                        modifier = Modifier
                            .size(16.dp)
                    )
                }
            }
            if (review.comment.isNotEmpty()) {
                Text(
                    text = review.comment,
                    style = MaterialTheme.typography.body1,
                    softWrap = true
                )
            }
        }
    }
}