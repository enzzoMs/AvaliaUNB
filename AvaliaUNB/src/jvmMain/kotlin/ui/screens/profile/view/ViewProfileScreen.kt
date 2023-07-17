package ui.screens.profile.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Report
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.models.ReportModel
import data.models.ReviewModel
import ui.components.buttons.SecondaryButton
import ui.components.forms.FormField
import ui.components.forms.GeneralTextField
import ui.components.review.ReviewCard
import ui.screens.profile.view.viewmodel.ViewProfileViewModel
import utils.resources.Colors
import utils.resources.Strings

private val REVIEW_LIST_HEIGHT = 450.dp

@Composable
fun ViewProfileScreen(
    viewProfileViewModel: ViewProfileViewModel,
    onDeleteAccount: () -> Unit,
    onBackClicked: () -> Unit
) {
    val viewProfileUiState by viewProfileViewModel.viewProfileUiState.collectAsState()

    Box {
        val stateVertical = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(Colors.DarkAntiFlashWhite)
                .verticalScroll(stateVertical)
        ) {
            UserInformation(
                userRegistrationNumber = viewProfileUiState.registrationNumber,
                userName = viewProfileUiState.name,
                userCourse = viewProfileUiState.course ?: "",
                userEmail = viewProfileUiState.email,
                userProfilePicture = viewProfileUiState.profilePic,
                showDeleteButton = viewProfileUiState.showDeleteButton,
                onDeleteClicked = onDeleteAccount,
                modifier = Modifier
                    .padding(top = 30.dp)
            )

            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 30.dp)
            )

            UserReportedReviews(
                reviews = viewProfileUiState.userReviews,
                onRemoveReview = { reviewModel ->  viewProfileViewModel.deleteReview(reviewModel) },
                onRemoveReport = { reportModel ->  viewProfileViewModel.deleteReport(reportModel) }
            )

            BackButton(
                onBackClicked = onBackClicked,
            )
        }

        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(stateVertical),
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
        )
    }

}

@Composable
private fun UserReportedReviews(
    reviews: List<ReviewModel>,
    onRemoveReview: (ReviewModel) -> Unit,
    onRemoveReport: (ReportModel) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(30.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .clip(RoundedCornerShape(percent = 15))
                    .background(Colors.UnbBlue)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Report,
                    contentDescription = null,
                    tint = Colors.White,
                    modifier = Modifier
                        .padding(5.dp)
                )
            }
            Text(
                text = Strings.REPORTS,
                style = MaterialTheme.typography.subtitle1,
                fontSize = 20.sp
            )
        }

        if (reviews.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 80.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Feedback,
                    contentDescription = null,
                    tint = Colors.MediumGray,
                    modifier = Modifier
                        .size(80.dp)
                )
                Text(
                    text = Strings.NO_REPORTS_ABOUT_USER,
                    style = MaterialTheme.typography.subtitle2,
                    color = Colors.SilverChalice,
                    modifier = Modifier
                        .padding(top = 16.dp)
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(REVIEW_LIST_HEIGHT)
            ) {
                val listState = rememberLazyListState()

                LazyColumn(
                    state = listState,
                ) {
                    items(reviews) { review ->
                        ReviewCard(
                            review = review,
                            showDeleteButton = true,
                            decideShowReport = { true },
                            onUserClicked = {},
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
                        .padding(top = 18.dp)
                )
            }
        }
    }
}


@Composable
private fun UserInformation(
    userRegistrationNumber: String,
    userName: String,
    userCourse: String,
    userEmail: String,
    userProfilePicture: ImageBitmap,
    showDeleteButton: Boolean,
    onDeleteClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .then(modifier)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = 32.dp,
                    end = 32.dp,
                    top = if (showDeleteButton) 0.dp else 102.dp
                )
        ) {
            if (showDeleteButton) {
                DeleteAccountButton(
                    onConfirmButtonClicked = onDeleteClicked
                )
            }

            Image(
                bitmap = userProfilePicture,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(250.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 32.dp, end = 32.dp, top = 22.dp)
        ) {
            // Registration Number Field -----------------------------------------
            FormField(
                title = Strings.FIELD_TITLE_REGISTRATION_NUMBER,
                textField = {
                    GeneralTextField(
                        value = userRegistrationNumber,
                        onValueChange = {},
                        enabled = false,
                        backgroundColor = Colors.LightGray,
                        startIcon = Icons.Outlined.Badge
                    )
                }
            )


            // Name Field -------------------------------------------------------
            FormField(
                title = Strings.FIELD_TITLE_NAME,
                modifier = Modifier
                    .padding(top = 22.dp),
                textField = {
                    GeneralTextField(
                        value = userName,
                        onValueChange = {},
                        enabled = false,
                        backgroundColor = Colors.LightGray,
                        startIcon = Icons.Filled.TextFields
                    )
                }
            )

            // Course Field -------------------------------------------------------
            FormField(
                title = Strings.FIELD_TITLE_COURSE,
                modifier = Modifier
                    .padding(top = 22.dp),
                textField = {
                    GeneralTextField(
                        value = userCourse,
                        onValueChange = {},
                        hintText = Strings.FIELD_HINT_NOT_INFORMED,
                        enabled = false,
                        backgroundColor = Colors.LightGray,
                        startIcon = Icons.Outlined.CollectionsBookmark
                    )
                }
            )

            // Email Field -------------------------------------------------------
            FormField(
                title = Strings.FIELD_TITLE_EMAIL,
                modifier = Modifier
                    .padding(top = 22.dp),
                textField = {
                    GeneralTextField(
                        value = userEmail,
                        onValueChange = {},
                        enabled = false,
                        backgroundColor = Colors.LightGray,
                        startIcon = Icons.Filled.Email
                    )
                }
            )
        }
    }
}



@Composable
private fun DeleteAccountButton(
    onConfirmButtonClicked: () -> Unit = {}
) {
    var confirmationVisible by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(bottom = 30.dp, top = 24.dp)
    ) {
        Button(
            onClick = { confirmationVisible = true } ,
            contentPadding = PaddingValues(vertical = 10.dp, horizontal = 15.dp),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                hoveredElevation = 0.dp,
                focusedElevation = 0.dp
            ),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.error,
                contentColor = Colors.White
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = null,
                tint = Colors.White,
                modifier = Modifier
                    .padding(end = 8.dp)
            )
            Text(
                text = Strings.ACTION_DELETE_ACCOUNT,
                style = MaterialTheme.typography.button,
                color = Colors.White
            )
        }

        AnimatedVisibility(confirmationVisible) {
            Row(
                modifier = Modifier
                    .padding(start = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = Colors.UnbGreen,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable { onConfirmButtonClicked() }
                )

                Spacer(
                    modifier = Modifier
                        .size(16.dp)
                )

                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colors.error,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable { confirmationVisible = false }
                )
            }
        }
    }
}


@Composable
private fun BackButton(
    onBackClicked: () -> Unit
) {
    Row {
        Spacer(
            modifier = Modifier
                .weight(1f)
        )

        SecondaryButton(
            label = Strings.CAPITALIZED_BACK,
            onClick = onBackClicked,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 20.dp)
        )
    }

}