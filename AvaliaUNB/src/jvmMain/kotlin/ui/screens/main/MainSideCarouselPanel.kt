package ui.screens.login

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import resources.StringResources
import ui.components.AutoSlidingImageCarousel

const val LOGIN_CAROUSEL_SLIDE_DURATION_MS = 6000L

@Composable
fun MainSideCarouselPanel() {
    AutoSlidingImageCarousel(
        imageItems = arrayOf(
            painterResource("images/rating_people.svg"),
            painterResource("images/rating_subjects.svg"),
            painterResource("images/students_talking.svg"),
            ),
        imageTitles = arrayOf(
            StringResources.PANEL_TEACHERS_CAPTION_TITLE,
            StringResources.PANEL_SUBJECTS_CAPTION_TITLE,
            StringResources.PANEL_STUDENTS_CAPTION_TITLE
        ),
        imageCaptions = arrayOf(
            StringResources.PANEL_TEACHERS_CAPTION,
            StringResources.PANEL_SUBJECTS_CAPTION,
            StringResources.PANEL_STUDENTS_CAPTION
        ),
        autoSlideDurationMs = LOGIN_CAROUSEL_SLIDE_DURATION_MS,
        modifier = Modifier
            .padding(end = 38.dp, start = 38.dp, top = 28.dp, bottom = 60.dp)
    )
}