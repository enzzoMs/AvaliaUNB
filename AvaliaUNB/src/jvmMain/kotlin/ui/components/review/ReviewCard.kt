package ui.components.review

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.models.ReviewModel
import theme.*
import ui.components.forms.GeneralDropDownMenu
import ui.components.forms.MultilineTextField
import utils.resources.ResourcesUtils

private const val MAX_SCORE_STARS = 5
private const val REVIEW_MAX_NUMBER_OF_CHAR = 850
private val FORM_HEIGHT = 150.dp

@Composable
fun ReviewCard(
    review: ReviewModel,
    showEditAndRemove: Boolean = false,
    onEditClicked: (ReviewModel, Int, String) -> Unit = { _: ReviewModel, _: Int, _: String -> },
    onRemoveClicked: (ReviewModel) -> Unit = {},
    userNameTextStyle: TextStyle = MaterialTheme.typography.subtitle2,
    backgroundColor: Color = White,
    modifier: Modifier = Modifier
) {
    var isEditingCard by remember { mutableStateOf(false) }
    var isDeletingCard by remember { mutableStateOf(false) }
    var selectedStarRating by remember { mutableStateOf(ResourcesUtils.Strings.STAR_RATINGS.find {
            rating -> rating.length == review.rating }!!)
    }
    var userEditComment by remember { mutableStateOf(review.comment) }

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
                UserInformation(review, userNameTextStyle)

                Spacer(
                    modifier = Modifier
                        .weight(1f)
                )

                if (showEditAndRemove && !isEditingCard && !isDeletingCard) {
                    EditRemoveButtons(
                        onEditClicked = {
                            isEditingCard = true
                        },
                        onRemoveClicked = { isDeletingCard = true }
                    )
                }
                if (isEditingCard || isDeletingCard) {
                    ConfirmCancelButtons(
                        onConfirmClicked = {
                            if (isDeletingCard) {
                                onRemoveClicked(review)
                            } else {
                                isEditingCard = false
                                onEditClicked(review, selectedStarRating.length, userEditComment)
                            }
                        },
                        onCancelClicked = {
                            isEditingCard = false
                            isDeletingCard = false
                            selectedStarRating = ResourcesUtils.Strings.STAR_RATINGS.find {
                                    rating -> rating.length == review.rating
                            }!!
                            userEditComment = review.comment
                        }
                    )
                }
            }

            if (isEditingCard) {
                ScoreDropdown(
                    selectedStarRating = selectedStarRating,
                    onSelectRating = { selectedStarRating = it }
                )
                CommentTextField(
                    value = userEditComment,
                    onValueChanged = { userEditComment = it }
                )
            } else {
                UserReviewScore(review.rating)
                UserReviewComment(review.comment)
            }
        }
    }
}

@Composable
private fun CommentTextField(
    value: String,
    onValueChanged: (String) -> Unit
) {
    MultilineTextField(
        value = value,
        onValueChange = onValueChanged,
        maxNumberOfCharacters = REVIEW_MAX_NUMBER_OF_CHAR,
        textFieldHeight = FORM_HEIGHT,
        hintText = ResourcesUtils.Strings.REVIEW_FORM_HINT
    )
}

@Composable
private fun ScoreDropdown(
    selectedStarRating: String,
    onSelectRating: (String) -> Unit,
) {
    Row {
        GeneralDropDownMenu(
            menuItems = ResourcesUtils.Strings.STAR_RATINGS,
            selectedItem = selectedStarRating,
            backgroundColor = AntiFlashWhite,
            onSelectItem = { onSelectRating(it) },
            selectedItemTextStyle = TextStyle(
                fontFamily = MaterialTheme.typography.subtitle2.fontFamily,
                fontSize = 16.sp,
                fontWeight = MaterialTheme.typography.subtitle2.fontWeight,
                color = AmericanOrange
            ),
            dropDownTextStyle = TextStyle(
                fontFamily = MaterialTheme.typography.subtitle2.fontFamily,
                fontSize = 16.sp,
                fontWeight = MaterialTheme.typography.subtitle2.fontWeight,
                color = AmericanOrange
            ),
            dropDownMenuMinWidth = 120.dp,
            dropDownMenuMinHeight = 250.dp,
            modifier = Modifier
                .weight(1f)
        )
        Spacer(
            modifier = Modifier
                .weight(5f)
        )
    }

}


@Composable
private fun UserReviewComment(
    comment: String
) {
    if (comment.isNotEmpty()) {
        Text(
            text = comment,
            style = MaterialTheme.typography.body1,
            softWrap = true
        )
    }
}


@Composable
private fun UserReviewScore(
    reviewRating: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(bottom = 12.dp)
    ) {
        repeat(reviewRating) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = AmericanOrange,
                modifier = Modifier
                    .size(16.dp)
            )
        }
        repeat(MAX_SCORE_STARS - reviewRating) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = Gray,
                modifier = Modifier
                    .size(16.dp)
            )
        }
    }
}

@Composable
private fun UserInformation(
    review: ReviewModel,
    userNameTextStyle: TextStyle
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
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
}

@Composable
private fun EditRemoveButtons(
    onEditClicked: () -> Unit,
    onRemoveClicked: () -> Unit
) {
    Icon(
        imageVector = Icons.Filled.Edit,
        contentDescription = null,
        tint = Gray,
        modifier = Modifier
            .clip(CircleShape)
            .clickable { onEditClicked() }
    )
    Icon(
        imageVector = Icons.Filled.Delete,
        contentDescription = null,
        tint = Gray,
        modifier = Modifier
            .padding(start = 6.dp)
            .clip(CircleShape)
            .clickable { onRemoveClicked() }
    )
}

@Composable
private fun ConfirmCancelButtons(
    onConfirmClicked: () -> Unit,
    onCancelClicked: () -> Unit
) {
    Icon(
        imageVector = Icons.Filled.Check,
        contentDescription = null,
        tint = UnbGreen,
        modifier = Modifier
            .clip(CircleShape)
            .clickable { onConfirmClicked() }
    )
    Icon(
        imageVector = Icons.Filled.Close,
        contentDescription = null,
        tint = MaterialTheme.colors.error,
        modifier = Modifier
            .padding(start = 6.dp)
            .clip(CircleShape)
            .clickable { onCancelClicked() }
    )
}