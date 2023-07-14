package ui.screens.entry

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ui.components.carousel.AutoSlidingImageCarousel
import utils.resources.Paths
import utils.resources.Strings

const val LOGIN_CAROUSEL_SLIDE_DURATION_MS = 6000L

@Composable
fun EntrySideCarouselPanel() {
    AutoSlidingImageCarousel(
        imageItems = arrayOf(
            painterResource(Paths.Images.RATING_PEOPLE),
            painterResource(Paths.Images.RATING_SUBJECTS),
            painterResource(Paths.Images.STUDENTS_TALKING),
            ),
        imageTitles = arrayOf(
            Strings.PANEL_TEACHERS_CAPTION_TITLE,
            Strings.PANEL_SUBJECTS_CAPTION_TITLE,
            Strings.PANEL_STUDENTS_CAPTION_TITLE
        ),
        imageCaptions = arrayOf(
            Strings.PANEL_TEACHERS_CAPTION,
            Strings.PANEL_SUBJECTS_CAPTION,
            Strings.PANEL_STUDENTS_CAPTION
        ),
        autoSlideDurationMs = LOGIN_CAROUSEL_SLIDE_DURATION_MS,
        modifier = Modifier
            .padding(end = 38.dp, start = 38.dp, top = 28.dp, bottom = 60.dp)
    )
}