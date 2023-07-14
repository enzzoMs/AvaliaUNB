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
import androidx.compose.material.icons.outlined.Report
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.models.ReportModel
import data.models.ReviewModel
import ui.components.forms.GeneralDropDownMenu
import ui.components.forms.MultilineTextField
import utils.Utils
import utils.resources.Colors
import utils.resources.Strings

private const val MAX_SCORE_STARS = 5
private const val REVIEW_MAX_NUMBER_OF_CHARS = 850
private const val REPORT_MAX_NUMBER_OF_CHARS = 200
private const val USER_NAME_MAX_LENGTH = 25
private val REVIEW_FORM_HEIGHT = 150.dp
private val REPORT_FORM_HEIGHT = 100.dp

@Composable
fun ReviewCard(
    review: ReviewModel,
    showEditAndRemove: Boolean = false,
    showReportButton: Boolean = false,
    onEditClicked: (ReviewModel, Int, String) -> Unit,
    onRemoveClicked: (ReviewModel) -> Unit,
    onReportClicked: (Int, String) -> Unit,
    onEditReportClicked: (Int, String) -> Unit,
    onRemoveUserReportClicked: (Int) -> Unit,
    onRemoveAnyReportClicked: (ReportModel) -> Unit = {},
    userReport: ReportModel? = null,
    showAllReports: Boolean = false,
    getAllReports: (ReviewModel) -> List<ReportModel>,
    userNameTextStyle: TextStyle = MaterialTheme.typography.subtitle2,
    backgroundColor: Color = Colors.White,
    userRegistrationNumber: String,
    modifier: Modifier = Modifier
) {
    var isEditingReview by remember { mutableStateOf(false) }
    var isDeletingReview by remember { mutableStateOf(false) }
    var isReportingReview by remember { mutableStateOf(false) }
    var isEditingReport by remember { mutableStateOf(false) }
    var userMadeReport by remember { mutableStateOf(userReport != null) }
    var userEditComment by remember { mutableStateOf(review.comment) }
    var userReportEditComment by remember { mutableStateOf(userReport?.description ?: "") }
    var reportComment by remember { mutableStateOf(userReportEditComment) }
    var selectedStarRating by remember { mutableStateOf(Strings.LIST_STAR_RATINGS.find {
            rating -> rating.length == review.rating }!!)
    }
    var allReports by remember { mutableStateOf(getAllReports(review)) }

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

                if (showReportButton && !isReportingReview && !userMadeReport) {
                    ReportButton { isReportingReview = true }
                }

                if (showEditAndRemove && !isEditingReview && !isDeletingReview) {
                    EditRemoveButtons(
                        onEditClicked = {
                            userEditComment = review.comment
                            selectedStarRating = Strings.LIST_STAR_RATINGS.find {
                                    rating -> rating.length == review.rating
                            }!!
                            isEditingReview = true
                        },
                        onRemoveClicked = { isDeletingReview = true }
                    )
                }
                if (isEditingReview || isDeletingReview) {
                    ConfirmCancelButtons(
                        onConfirmClicked = {
                            if (isDeletingReview) {
                                isEditingReview = false
                                isDeletingReview = false
                                onRemoveClicked(review)
                            } else {
                                isEditingReview = false
                                onEditClicked(review, selectedStarRating.length, userEditComment)
                            }
                        },
                        onCancelClicked = {
                            isEditingReview = false
                            isDeletingReview = false
                            selectedStarRating = Strings.LIST_STAR_RATINGS.find {
                                    rating -> rating.length == review.rating
                            }!!
                            userEditComment = review.comment
                        }
                    )
                }
            }

            if (isEditingReview) {
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

                if (isReportingReview || isEditingReport) {
                    ReportTextField(
                        reportText = reportComment,
                        isEditingReport = isEditingReport,
                        onReportTextChanged = { reportComment = it },
                        onCancelClicked = {
                            reportComment = if (isReportingReview) "" else userReportEditComment
                            isEditingReport = false
                            isReportingReview = false
                        },
                        onConfirmClicked = {
                            if (isReportingReview) {
                                onReportClicked(review.id, reportComment)
                            } else {
                                onEditReportClicked(review.id, reportComment)
                            }

                            userMadeReport = true
                            isReportingReview = false
                            isEditingReport = false
                            userReportEditComment = reportComment
                        }
                    )
                } else if (getAllReports(review).find { it.userRegistrationNumber == userRegistrationNumber } != null) {
                    val report = getAllReports(review).find { it.userRegistrationNumber == userRegistrationNumber }

                    ReportCard(
                        reportTitle = Strings.FIELD_PREFIX_REPORT_MADE,
                        description = report?.description ?: "",
                        showEdit = true,
                        onEditClicked = { isEditingReport = true },
                        onDeleteClicked = {
                            reportComment = ""
                            userMadeReport = false
                            onRemoveUserReportClicked(review.id)
                        }
                    )
                }

                if (showAllReports) {
                    for (report in allReports) {
                        ReportCard(
                            reportTitle = "${report.userName}  fez uma denúncia: ",
                            description = report.description,
                            onDeleteClicked = {
                                onRemoveAnyReportClicked(report)
                                allReports = getAllReports(review)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReportCard(
    reportTitle: String,
    description: String,
    showEdit: Boolean = false,
    onEditClicked: () -> Unit = {},
    onDeleteClicked: () -> Unit
) {
    var isRemovingReport by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .padding(top = 30.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Report,
                contentDescription = null,
                tint = MaterialTheme.colors.error,
                modifier = Modifier
                    .padding(end = 6.dp)
            )
            Text(
                text = reportTitle,
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.error
            )

            Spacer(
                modifier = Modifier
                    .weight(1f)
            )

            when {
                isRemovingReport -> {
                    ConfirmCancelButtons(
                        onConfirmClicked = onDeleteClicked,
                        onCancelClicked = { isRemovingReport = false }
                    )
                }
                showEdit -> {
                    EditRemoveButtons(
                        onEditClicked = onEditClicked,
                        onRemoveClicked = { isRemovingReport = true }
                    )
                }
                else -> {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null,
                        tint = Colors.DimGray,
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .clip(CircleShape)
                            .clickable { isRemovingReport = true }
                    )
                }
            }
        }

        Text(
            text = description,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.error,
            softWrap = true,
            modifier = Modifier
                .padding(top = 14.dp)
        )
    }
}


@Composable
private fun ReportTextField(
    reportText: String,
    isEditingReport: Boolean,
    onReportTextChanged: (String) -> Unit,
    onConfirmClicked: () -> Unit,
    onCancelClicked: () -> Unit
) {
    var isReportEmpty by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .padding(top = 30.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Report,
                contentDescription = null,
                tint = MaterialTheme.colors.error,
                modifier = Modifier
                    .padding(end = 6.dp)
            )
            Text(
                text = if (isEditingReport) {
                    Strings.FIELD_PREFIX_REPORT_MADE
                } else {
                    Strings.FIELD_PREFIX_REPORT_REVIEW
                },
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.error
            )

            Spacer(
                modifier = Modifier
                    .weight(1f)
            )

            ConfirmCancelButtons(
                onConfirmClicked = {
                    if (reportText.isEmpty()) isReportEmpty = true else onConfirmClicked()
                },
                onCancelClicked = onCancelClicked
            )
        }

        MultilineTextField(
            value = reportText,
            onValueChange = {
                isReportEmpty = false
                onReportTextChanged(it)
            },
            error = isReportEmpty,
            errorMessage = Strings.FIELD_ERROR_REQUIRED_DESCRIPTION,
            maxNumberOfCharacters = REPORT_MAX_NUMBER_OF_CHARS,
            showCharacterCount = false,
            textFieldHeight = REPORT_FORM_HEIGHT,
            hintText = Strings.FIELD_HINT_REVIEW_REPORT
        )
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
        maxNumberOfCharacters = REVIEW_MAX_NUMBER_OF_CHARS,
        textFieldHeight = REVIEW_FORM_HEIGHT,
        hintText = Strings.FIELD_HINT_REVIEW
    )
}

@Composable
private fun ScoreDropdown(
    selectedStarRating: String,
    onSelectRating: (String) -> Unit,
) {
    Row {
        GeneralDropDownMenu(
            menuItems = Strings.LIST_STAR_RATINGS,
            selectedItem = selectedStarRating,
            backgroundColor = Colors.AntiFlashWhite,
            onSelectItem = { onSelectRating(it) },
            selectedItemTextStyle = TextStyle(
                fontFamily = MaterialTheme.typography.subtitle2.fontFamily,
                fontSize = 16.sp,
                fontWeight = MaterialTheme.typography.subtitle2.fontWeight,
                color = Colors.AmericanOrange
            ),
            dropDownTextStyle = TextStyle(
                fontFamily = MaterialTheme.typography.subtitle2.fontFamily,
                fontSize = 16.sp,
                fontWeight = MaterialTheme.typography.subtitle2.fontWeight,
                color = Colors.AmericanOrange
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
                tint = Colors.AmericanOrange,
                modifier = Modifier
                    .size(16.dp)
            )
        }
        repeat(MAX_SCORE_STARS - reviewRating) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = Colors.Gray,
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
            bitmap = review.userProfilePicture ?: Utils.getDefaultProfilePicture(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(40.dp)
                .padding(end = 10.dp)
        )
        UserName(
            userName = review.userName,
            userNameTextStyle = userNameTextStyle
        )

        Text(
            text = Strings.FIELD_PREFIX_PUBLISHED,
            style = userNameTextStyle
        )
    }
}

@Composable
private fun UserName(
    userName: String,
    userNameTextStyle: TextStyle
) {
    val nameSplit = userName.split(" ")

    var name = ""

    for (part in nameSplit) {
        name += if ("$name$part".length <= USER_NAME_MAX_LENGTH) "$part " else ""
    }

    if (name.isEmpty()) {
        name = nameSplit[0].substring(0, USER_NAME_MAX_LENGTH) + "..."
    }

    Text(
        text = name,
        style = userNameTextStyle,
        color = Colors.UnbBlue,
        modifier = Modifier
            .padding(end = 10.dp)
    )
}

@Composable
private fun ReportButton(
    onReportClicked: () -> Unit
) {
    Icon(
        imageVector = Icons.Filled.Report,
        contentDescription = null,
        tint = MaterialTheme.colors.error,
        modifier = Modifier
            .clip(CircleShape)
            .clickable { onReportClicked() }
    )
}

@Composable
private fun EditRemoveButtons(
    onEditClicked: () -> Unit,
    onRemoveClicked: () -> Unit
) {
    Icon(
        imageVector = Icons.Filled.Edit,
        contentDescription = null,
        tint = Colors.DimGray,
        modifier = Modifier
            .clip(CircleShape)
            .clickable { onEditClicked() }
    )
    Icon(
        imageVector = Icons.Filled.Delete,
        contentDescription = null,
        tint = Colors.DimGray,
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
        tint = Colors.UnbGreen,
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