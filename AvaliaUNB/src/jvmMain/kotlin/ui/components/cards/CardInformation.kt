package ui.components.cards

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CardInformation(
    fieldName: String,
    fieldNameTextStyle: TextStyle,
    fieldText: String,
    fieldTextStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = fieldName,
            style = fieldNameTextStyle,
            modifier = Modifier
                .padding(end = 10.dp)
        )
        Text(
            text = fieldText,
            style = fieldTextStyle,
            fontWeight = FontWeight.Normal
        )
    }
}