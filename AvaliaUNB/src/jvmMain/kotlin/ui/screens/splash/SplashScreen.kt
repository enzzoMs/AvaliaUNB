package ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import resources.StringResources
import theme.UnbBlue
import theme.UnbGreen
import ui.components.AnimatedSplashScreen

const val SPLASH_ANIMATION_DURATION_MS = 2000

@Composable
fun SplashScreen() {
    AnimatedSplashScreen(
        splashAnimationDurationMs = SPLASH_ANIMATION_DURATION_MS,
        targetValue = 0.9f,
        modifier = Modifier
            .background(Color.White),
        splashContent = { scaleModifier ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = scaleModifier
            ) {
                Image (
                    painter = painterResource("images/logo_avalia_unb.svg"),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(180.dp)
                )
                Text(
                    modifier = Modifier,
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = UnbBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 56.sp
                            )
                        ) {
                            append(StringResources.splashScreenFirstAppTitle)
                        }

                        withStyle(
                            style = SpanStyle(
                                color = UnbGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 56.sp
                            )
                        ) {
                            append(StringResources.splashScreenSecondAppTitle)
                        }
                    }
                )
            }
        }
    )
}