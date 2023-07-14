package utils.resources

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.sp

val QuicksandFont = FontFamily(
    Font(Paths.Fonts.QUICKSAND_BOLD, FontWeight.Bold),
    Font(Paths.Fonts.QUICKSAND_SEMI_BOLD, FontWeight.SemiBold),
    Font(Paths.Fonts.QUICKSAND_REGULAR, FontWeight.Normal)
)

val Typography = Typography(
    defaultFontFamily = QuicksandFont,

    h4 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        color = Colors.DarkCharcoal
    ),

    h6 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        color = Colors.DarkCharcoal
    ),

    subtitle1 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        color = Colors.DarkCharcoal
    ),

    subtitle2 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        color = Colors.Gray
    ),

    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Colors.DarkCharcoal
    ),

    body2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Colors.DimGray
    ),

    button = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        color = Colors.White
    )
)