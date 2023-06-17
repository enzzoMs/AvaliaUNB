package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import theme.AntiFlashWhite
import theme.DimGray
import theme.Gray
import theme.UnbGreen

@Composable
fun GeneralTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hintText: String = "",
    hintTextColor: Color = Gray,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    startIcon: ImageVector? = null,
    endIcon: ImageVector? = null,
    onEndIconClicked: (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    iconTint: Color = DimGray,
    borderColor: Color = UnbGreen,
    modifier: Modifier = Modifier
) {
    var hint by remember { mutableStateOf(hintText) }
    var border by remember { mutableStateOf(Transparent) }

    BasicTextField(
        value = value,
        maxLines = 1,
        onValueChange = onValueChange,
        visualTransformation = visualTransformation,
        textStyle = textStyle,
        modifier = Modifier
            .onFocusChanged { focusState ->
                hint = if (focusState.hasFocus) "" else hintText
                border = if (focusState.hasFocus) borderColor else Transparent
            }
            .border(
                width = 2.dp,
                color = border,
                shape = RoundedCornerShape(6.dp)
            ),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(AntiFlashWhite)
                    .padding(vertical = 8.dp, horizontal = 10.dp)
                    .fillMaxWidth()
                    .then(modifier)
            ) {
                if (startIcon != null) {
                    Icon(
                        imageVector = startIcon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier
                            .padding(end = 10.dp)
                    )
                }
                if (value.isEmpty()) {
                    Text(
                        text = hint,
                        style = textStyle,
                        color = hintTextColor
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    innerTextField()
                }

                if (endIcon != null) {
                    Icon(
                        imageVector = endIcon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .clip(CircleShape)
                            .clickable {
                                if (onEndIconClicked != null) {
                                    onEndIconClicked()
                                }
                            }
                    )
                }
            }
        }
    )
}