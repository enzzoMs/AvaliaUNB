package ui.screens.entry

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import utils.resources.ResourcesUtils
import ui.components.carousel.AutoSlidingImageCarousel

const val LOGIN_CAROUSEL_SLIDE_DURATION_MS = 6000L

@Composable
fun EntrySideCarouselPanel() {
    AutoSlidingImageCarousel(
        imageItems = arrayOf(
            painterResource(ResourcesUtils.ImagePaths.RATING_PEOPLE),
            painterResource(ResourcesUtils.ImagePaths.RATING_SUBJECTS),
            painterResource(ResourcesUtils.ImagePaths.STUDENTS_TALKING),
            ),
        imageTitles = arrayOf(
            ResourcesUtils.Strings.PANEL_TEACHERS_CAPTION_TITLE,
            ResourcesUtils.Strings.PANEL_SUBJECTS_CAPTION_TITLE,
            ResourcesUtils.Strings.PANEL_STUDENTS_CAPTION_TITLE
        ),
        imageCaptions = arrayOf(
            ResourcesUtils.Strings.PANEL_TEACHERS_CAPTION,
            ResourcesUtils.Strings.PANEL_SUBJECTS_CAPTION,
            ResourcesUtils.Strings.PANEL_STUDENTS_CAPTION
        ),
        autoSlideDurationMs = LOGIN_CAROUSEL_SLIDE_DURATION_MS,
        modifier = Modifier
            .padding(end = 38.dp, start = 38.dp, top = 28.dp, bottom = 60.dp)
    )
}