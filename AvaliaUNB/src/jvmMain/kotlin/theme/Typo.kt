package theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val QuicksandFont = FontFamily(
    Font("fonts/Quicksand_Bold.ttf", FontWeight.Bold),
    Font("fonts/Quicksand_SemiBold.ttf", FontWeight.SemiBold),
    Font("fonts/Quicksand_Regular.ttf", FontWeight.Normal)
)

val Typography = Typography(
    defaultFontFamily = QuicksandFont,

    h4 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 38.sp,
        color = DarkCharcoal
    ),

    h6 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        color = DarkCharcoal
    ),

    subtitle1 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        color = DarkCharcoal
    ),

    subtitle2 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        color = Gray
    ),

    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = DarkCharcoal
    ),

    body2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = DimGray
    ),

    button = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        color = White
    )
)