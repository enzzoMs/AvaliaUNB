package ui.components.forms

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
import utils.resources.Colors

@Composable
fun GeneralTextField(
    value: String,
    onValueChange: (String) -> Unit,
    error: Boolean = false,
    hintText: String = "",
    hintTextColor: Color = Colors.DimGray,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    startIcon: ImageVector? = null,
    endIcon: ImageVector? = null,
    onEndIconClicked: (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    iconTint: Color = Colors.DimGray,
    focusedBorderColor: Color = Colors.UnbGreen,
    backgroundColor: Color = Colors.AntiFlashWhite,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    var hint by remember { mutableStateOf(hintText) }
    var border by remember { mutableStateOf(Transparent) }

    BasicTextField(
        value = value,
        singleLine = true,
        enabled = enabled,
        onValueChange = onValueChange,
        visualTransformation = visualTransformation,
        textStyle = textStyle,
        modifier = Modifier
            .onFocusChanged { focusState ->
                hint = if (focusState.hasFocus) "" else hintText
                border = if (focusState.hasFocus) focusedBorderColor else Transparent
            }
            .border(
                width = 2.dp,
                color = if (error) MaterialTheme.colors.error else border,
                shape = RoundedCornerShape(6.dp)
            ),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(backgroundColor)
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