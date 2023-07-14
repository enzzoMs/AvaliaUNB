package ui.components.review

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.components.forms.GeneralDropDownMenu
import ui.components.forms.MultilineTextField
import utils.resources.Colors
import utils.resources.Strings

private const val REVIEW_MAX_NUMBER_OF_CHAR = 850
private val FORM_HEIGHT = 150.dp

@Composable
fun ReviewForm(
    value: String = "",
    onValueChanged: (String) -> Unit = {},
    error: Boolean = false,
    errorMessage: String? = null,
    onPublishClicked: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedStarRating by remember { mutableStateOf(Strings.LIST_STAR_RATINGS.first()) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
    ) {
        MultilineTextField(
            value = value,
            onValueChange = onValueChanged,
            error = error,
            errorMessage = errorMessage,
            maxNumberOfCharacters = REVIEW_MAX_NUMBER_OF_CHAR,
            textFieldHeight = FORM_HEIGHT,
            hintText = Strings.FIELD_HINT_REVIEW
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            Text(
                text = Strings.FIELD_PREFIX_RATING,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .padding(end = 12.dp)
            )
            GeneralDropDownMenu(
                menuItems = Strings.LIST_STAR_RATINGS,
                selectedItem = selectedStarRating,
                onSelectItem = { selectedStarRating = it },
                selectedItemTextStyle = TextStyle(
                    fontFamily = MaterialTheme.typography.subtitle2.fontFamily,
                    fontSize = 16.sp,
                    fontWeight = MaterialTheme.typography.subtitle2.fontWeight,
                    color = Colors.AmericanOrange
                ),
                dropDownTextStyle = TextStyle(
                    fontFamily = MaterialTheme.typography.subtitle2.fontFamily,
                    fontSize = 16.sp,
                    fontWeight = MaterialTheme.typography.subtitle2.fontWeight,
                    color = Colors.AmericanOrange
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
                onClick = {
                    focusManager.clearFocus()
                    onPublishClicked(value, selectedStarRating.length)
                },
                shape = RoundedCornerShape(4.dp),
                contentPadding = PaddingValues(vertical = 6.dp, horizontal = 20.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Colors.DarkCornflowerBlue,
                    contentColor = Colors.White
                ),
                modifier = Modifier
                    .weight(2f)
            ) {
                Text(
                    text = Strings.ACTION_PUBLISH,
                    style = MaterialTheme.typography.button
                )
            }
        }
    }

}