package ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import theme.White

data class NavigationPanelColors(
    val backgroundColor: Color,
    val selectedItemColor: Color,
    val unSelectedItemColor: Color,
    val selectedTextColor: Color,
    val unSelectedTextColor: Color,
    val selectedIndicatorColor: Color
)

data class NavigationItem(
    val label: String,
    val icon: ImageVector? = null,
    val index: Int
)

@Composable
fun SideNavigationPanel(
    selectedIndex: Int? = null,
    contentTop: (@Composable () -> Unit) = {},
    contentBottom: (@Composable () -> Unit) = {},
    onItemClicked: (NavigationItem) -> Unit = {},
    navPanelColors: NavigationPanelColors = NavigationPanelColors(
        backgroundColor = MaterialTheme.colors.primary,
        selectedItemColor = MaterialTheme.colors.onPrimary,
        unSelectedItemColor = MaterialTheme.colors.primaryVariant,
        selectedTextColor = MaterialTheme.colors.secondary,
        unSelectedTextColor = MaterialTheme.colors.secondaryVariant,
        selectedIndicatorColor = MaterialTheme.colors.onPrimary
    ),
    navItems: List<NavigationItem>,
    textStyle: TextStyle = MaterialTheme.typography.h6,
    modifier: Modifier = Modifier
) {
   Column(
       modifier = Modifier
           .background(navPanelColors.backgroundColor)
           .then(modifier)
   ) {
       contentTop()

       navItems.forEachIndexed  { index, navItem ->
           NavigationItem(
               label = navItem.label,
               onClick = { onItemClicked(navItem) },
               textStyle = textStyle,
               icon = navItem.icon,
               isSelected = index == selectedIndex,
               selectedColor = navPanelColors.selectedItemColor,
               unSelectedColor = navPanelColors.unSelectedItemColor,
               selectedTextColor = navPanelColors.selectedTextColor,
               unSelectedTextColor = navPanelColors.unSelectedTextColor,
               selectedIndicatorColor = navPanelColors.selectedIndicatorColor
           )
       }

       Spacer(modifier = Modifier
           .weight(1f)
       )

       contentBottom()
   }
}

@Composable
fun NavigationItem(
    label: String,
    onClick: () -> Unit = {},
    textStyle: TextStyle = MaterialTheme.typography.subtitle2,
    icon: ImageVector? = null,
    isSelected: Boolean = false,
    selectedColor: Color = MaterialTheme.colors.onPrimary,
    unSelectedColor: Color = MaterialTheme.colors.primaryVariant,
    selectedTextColor: Color = MaterialTheme.colors.secondary,
    unSelectedTextColor: Color = MaterialTheme.colors.secondaryVariant,
    selectedIndicatorColor: Color = MaterialTheme.colors.onPrimary,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(if (isSelected) selectedColor else unSelectedColor)
            .height(IntrinsicSize.Min)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = White),
            ) { onClick() }
            .then(modifier)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(4.dp)
                .background(if (isSelected) selectedIndicatorColor else unSelectedColor)
        )

        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) selectedTextColor else unSelectedTextColor,
                modifier = Modifier
                    .padding(start = 20.dp)
            )
        }

        Text(
            text = label,
            style = textStyle,
            color = if (isSelected) selectedTextColor else unSelectedTextColor,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(
                    start = if (icon == null) 20.dp else 14.dp,
                    top = 15.dp,
                    bottom = 15.dp,
                    end = 20.dp
                )
        )

        Spacer(
            modifier = Modifier
                .weight(1f)
        )
    }
}










