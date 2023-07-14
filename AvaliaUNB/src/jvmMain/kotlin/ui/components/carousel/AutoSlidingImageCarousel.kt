package ui.components.carousel

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import utils.resources.Colors

@Composable
fun AutoSlidingImageCarousel(
    imageItems: Array<Painter>,
    imageTitles: Array<String>? = null,
    imageCaptions: Array<String>? = null,
    autoSlideDurationMs: Long,
    modifier: Modifier
) {
    if (imageTitles != null && imageTitles.size != imageItems.size ||
        imageCaptions != null && imageCaptions.size != imageItems.size) {
        throw IllegalArgumentException("If not null, titles and captions should have the same length of imageItems")
    }

    var currentImageIndex by remember { mutableStateOf(0) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        Crossfade(
            targetState = currentImageIndex,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) { imageIndex ->
            Column {
                Image(
                    painter = imageItems[imageIndex],
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .padding(top = 72.dp, start = 72.dp, end = 72.dp, bottom = 65.dp)
                )

                imageTitles?.get(imageIndex)?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.subtitle2,
                        color = Colors.White,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier
                            .padding(bottom = 14.dp)
                    )
                }
                imageCaptions?.get(imageIndex)?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.body1,
                        color = Colors.White,
                        textAlign = TextAlign.Justify
                    )
                }
            }
        }
        GroupIndicatorDots(
            numberOfDots = imageItems.size,
            selectedDotIndex = currentImageIndex,
            modifier = Modifier
                .padding(top = 58.dp)
        )
    }

    LaunchedEffect(Unit) {
        while(true) {
            delay(autoSlideDurationMs)
            currentImageIndex = if (currentImageIndex != imageItems.size-1) currentImageIndex + 1 else 0
        }
    }

}

@Composable
private fun GroupIndicatorDots(
    dotSize: Dp = 12.dp,
    numberOfDots: Int,
    dotPadding: Dp = 8.dp,
    selectedDotIndex: Int = 0,
    dotSelectedColor: Color = Colors.Green,
    dotUnSelectedColor: Color = Colors.LightSilver,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        for (i in 0 until numberOfDots) {
            IndicatorDot(
                color = if (i == selectedDotIndex) dotSelectedColor else dotUnSelectedColor,
                size = dotSize
            )

            if (i < numberOfDots-1) {
                Spacer(
                    modifier = Modifier
                        .padding(end = dotPadding)
                )
            }

        }
    }
}


@Composable
private fun IndicatorDot(
    color: Color,
    size: Dp
) {
    Box(modifier = Modifier
        .clip(CircleShape)
        .background(color)
        .size(size)
        .padding(15.dp)
    )
}



