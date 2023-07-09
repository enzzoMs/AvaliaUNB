package ui.components.forms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import theme.AmericanOrange
import theme.DarkCornflowerBlue
import theme.White
import utils.resources.ResourcesUtils

private const val REVIEW_MAX_NUMBER_OF_CHAR = 850
private val FORM_HEIGHT = 150.dp

@Composable
fun ReviewForm(
    modifier: Modifier = Modifier
) {
    var reviewText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
    ) {
        MultilineTextField(
            value = reviewText,
            onValueChange = { reviewText = it },
            maxNumberOfCharacters = REVIEW_MAX_NUMBER_OF_CHAR,
            textFieldHeight = FORM_HEIGHT,
            hintText = ResourcesUtils.Strings.REVIEW_FORM_HINT
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            Text(
                text = ResourcesUtils.Strings.SCORE,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .padding(end = 12.dp)
            )
            GeneralDropDownMenu(
                menuItems = ResourcesUtils.Strings.STAR_RATINGS,
                selectedItem = ResourcesUtils.Strings.STAR_RATINGS.first(),
                onSelectItem = { },
                selectedItemTextStyle = TextStyle(
                    fontFamily = MaterialTheme.typography.subtitle2.fontFamily,
                    fontSize = 16.sp,
                    fontWeight = MaterialTheme.typography.subtitle2.fontWeight,
                    color = AmericanOrange
                ),
                dropDownTextStyle = TextStyle(
                    fontFamily = MaterialTheme.typography.subtitle2.fontFamily,
                    fontSize = 16.sp,
                    fontWeight = MaterialTheme.typography.subtitle2.fontWeight,
                    color = AmericanOrange
                ),
                dropDownMenuMinWidth = 120.dp,
                dropDownMenuMinHeight = 250.dp,
                modifier = Modifier
                    .weight(1.2f)
            )
            Spacer(
                modifier = Modifier
                    .weight(5f)
            )
            Button(
                onClick = {},
                shape = RoundedCornerShape(4.dp),
                contentPadding = PaddingValues(vertical = 6.dp, horizontal = 20.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = DarkCornflowerBlue,
                    contentColor = White
                ),
                modifier = Modifier
                    .weight(2f)
            ) {
                Text(
                    text = ResourcesUtils.Strings.PUBLISH,
                    style = MaterialTheme.typography.button
                )
            }
        }
    }

}