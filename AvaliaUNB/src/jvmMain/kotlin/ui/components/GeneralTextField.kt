package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import theme.AntiFlashWhite
import theme.DimGray
import theme.Gray

@Composable
fun GeneralTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hintText: String = "",
    hintTextColor: Color = Gray,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    startIcon: ImageVector? = null,
    endIcon: ImageVector? = null,
    iconTint: Color = DimGray,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle,
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
                        text = hintText,
                        style = textStyle,
                        color = hintTextColor
                    )
                }

                innerTextField()

                if (endIcon != null) {
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )
                    Icon(
                        imageVector = endIcon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier
                            .padding(start = 10.dp)
                    )
                }
            }
        }
    )
}