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
import androidx.compose.ui.graphics.ImageBitmap
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
    showEditButton: Boolean = false,
    showDeleteButton: Boolean = false,
    showReportButton: Boolean = false,
    onEditClicked: (ReviewModel, Int, String) -> Unit,
    onRemoveClicked: (ReviewModel) -> Unit,
    onReportClicked: (Int, String) -> Unit,
    onEditReportClicked: (ReportModel, String) -> Unit,
    onRemoveReportClicked: (ReportModel) -> Unit,
    decideShowReport: (ReportModel) -> Boolean,
    decideShowEditReport: (ReportModel) -> Boolean,
    decideShowRemoveReport: (ReportModel) -> Boolean,
    userNameTextStyle: TextStyle = MaterialTheme.typography.subtitle2,
    backgroundColor: Color = Colors.White,
    modifier: Modifier = Modifier
) {
    var isEditingReview by remember { mutableStateOf(false) }
    var isDeletingReview by remember { mutableStateOf(false) }
    var isReportingReview by remember { mutableStateOf(false) }

    var reviewEditComment by remember { mutableStateOf("") }
    var reviewStarRating by remember { mutableStateOf("") }

    var reportComment by remember { mutableStateOf("") }

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
                UserInformation(
                    userName = review.userName,
                    userProfilePicture = review.userProfilePicture,
                    userNameTextStyle = userNameTextStyle
                )

                Spacer(
                    modifier = Modifier
                        .weight(1f)
                )

                if (showReportButton && !isReportingReview) {
                    ReportButton {
                        reportComment = ""
                        isReportingReview = true
                    }
                }

                if (!isEditingReview && !isDeletingReview) {
                    if (showEditButton) {
                        EditButton {
                            reviewEditComment = review.comment
                            reviewStarRating = Strings.LIST_STAR_RATINGS.find {
                                    rating -> rating.length == review.rating
                            }!!
                            isEditingReview = true
                        }
                    }

                    if (showDeleteButton) {
                        DeleteButton { isDeletingReview = true }
                    }
                } else {
                    ConfirmCancelButtons(
                        onConfirmClicked = {
                            if (isDeletingReview) {
                                isDeletingReview = false
                                onRemoveClicked(review)
                            } else {
                                isEditingReview = false
                                onEditClicked(review, reviewStarRating.length, reviewEditComment)
                            }
                        },
                        onCancelClicked = {
                            isEditingReview = false
                            isDeletingReview = false
                        }
                    )
                }
            }

            if (isEditingReview) {
                ScoreDropdown(
                    selectedStarRating = reviewStarRating,
                    onSelectRating = { reviewStarRating = it }
                )
                CommentTextField(
                    value = reviewEditComment,
                    onValueChanged = { reviewEditComment = it }
                )
            } else {
                UserReviewScore(review.rating)
                UserReviewComment(review.comment)
            }

            if (isReportingReview) {
                ReportTextField(
                    fieldTitle = Strings.FIELD_PREFIX_REPORT_REVIEW,
                    reportText = reportComment,
                    onReportTextChanged = { reportComment = it },
                    onCancelClicked = {
                        isReportingReview = false
                    },
                    onConfirmClicked = {
                        isReportingReview = false
                        onReportClicked(review.id, reportComment)
                    },
                    modifier = Modifier
                        .padding(top = 30.dp)
                )
            }


            for (report in review.reports) {
                if (decideShowReport(report)) {
                    ReportCard(
                        report = report,
                        showDelete = decideShowRemoveReport(report),
                        showEdit = decideShowEditReport(report),
                        onEditClicked = { reportModel: ReportModel, reportDescription ->
                            onEditReportClicked(reportModel, reportDescription)
                        },
                        onDeleteClicked = { reportModel: ReportModel ->
                            onRemoveReportClicked(reportModel)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ReportCard(
    report: ReportModel,
    showEdit: Boolean = false,
    showDelete: Boolean = false,
    onEditClicked: (ReportModel, String) -> Unit = { _: ReportModel, _: String -> },
    onDeleteClicked: (ReportModel) -> Unit = {}
) {
    var isDeleting by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }
    var reportEditComment by remember { mutableStateOf("") }

    Column (
        modifier = Modifier
            .padding(top = 30.dp)
    ) {
        if (!isEditing) {
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
                    text = if (showEdit) {
                        Strings.FIELD_PREFIX_REPORT_MADE
                    } else {
                        "${report.userRegistrationNumber}  fez uma denúncia:"
                    },
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.error
                )

                Spacer(
                    modifier = Modifier
                        .weight(1f)
                )


                if (!isDeleting) {
                    if (showEdit) {
                        EditButton {
                            reportEditComment = report.description
                            isEditing = true
                        }
                    }

                    if (showDelete) {
                        DeleteButton { isDeleting = true }
                    }
                } else {
                    ConfirmCancelButtons(
                        onConfirmClicked = {
                            if (isDeleting) {
                                isDeleting = false
                                onDeleteClicked(report)
                            } else {
                                isEditing = false
                                onEditClicked(report, reportEditComment)
                            }
                        },
                        onCancelClicked = {
                            isEditing = false
                            isDeleting = false
                        }
                    )
                }
            }

            Text(
                text = report.description,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.error,
                softWrap = true,
                modifier = Modifier
                    .padding(top = 14.dp)
            )

        } else {
            ReportTextField(
                fieldTitle = Strings.FIELD_PREFIX_REPORT_MADE,
                reportText = reportEditComment,
                onReportTextChanged = { reportEditComment = it },
                onCancelClicked = {
                    isEditing = false
                },
                onConfirmClicked = {
                    isEditing = false
                    onEditClicked(report, reportEditComment)
                }
            )
        }
    }
}


@Composable
private fun ReportTextField(
    fieldTitle: String = "",
    reportText: String,
    onReportTextChanged: (String) -> Unit,
    onConfirmClicked: () -> Unit,
    onCancelClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isReportEmpty by remember { mutableStateOf(false) }

    Column (
        modifier = modifier
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
                text = fieldTitle,
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
    userName: String,
    userProfilePicture: ImageBitmap?,
    userNameTextStyle: TextStyle
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            bitmap = userProfilePicture ?: Utils.getDefaultProfilePicture(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(40.dp)
                .padding(end = 10.dp)
        )
        UserName(
            userName = userName,
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
private fun EditButton(
    onEditClicked: () -> Unit
) {
    Icon(
        imageVector = Icons.Filled.Edit,
        contentDescription = null,
        tint = Colors.DimGray,
        modifier = Modifier
            .clip(CircleShape)
            .clickable { onEditClicked() }
    )
}

@Composable
private fun DeleteButton (
    onRemoveClicked: () -> Unit
) {
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