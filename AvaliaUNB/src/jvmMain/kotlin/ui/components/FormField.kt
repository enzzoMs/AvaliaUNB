package ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import utils.resources.ResourcesUtils

@Composable
fun FormField(
    title: String,
    titleStyle: TextStyle = MaterialTheme.typography.subtitle1,
    optional: Boolean = false,
    optionalTextStyle: TextStyle? = null,
    error: Boolean = false,
    errorMessage: String? = null,
    textField: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        Text(
            text = title,
            style = titleStyle,
            modifier = Modifier
                .padding(bottom = 12.dp)
                .alignByBaseline()
        )

        if (optional) {
            Text(
                text = ResourcesUtils.Strings.OPTIONAL_FIELD,
                style = optionalTextStyle ?: MaterialTheme.typography.subtitle2,
                fontSize = optionalTextStyle?.fontSize ?: 14.sp,
                modifier = Modifier
                    .padding(bottom = 12.dp, start = 4.dp)
                    .alignByBaseline()
            )
        }

        if (error && errorMessage != null) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(bottom = 2.dp)
                    .alignByBaseline()
                    .align(Alignment.CenterVertically)
            )
        }
    }

    textField()
}





