package ui.components.forms

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import theme.DimGray
import theme.LightSilver
import theme.UnbGreen
import theme.White

@Composable
fun MultilineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    error: Boolean = false,
    textFieldHeight: Dp = 100.dp,
    maxNumberOfCharacters: Int? = null,
    showCharacterCount: Boolean = true,
    hintText: String = "",
    hintTextColor: Color = DimGray,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    borderColor: Color = LightSilver,
    focusedBorderColor: Color = UnbGreen,
    backgroundColor: Color = White,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    var hint by remember { mutableStateOf(hintText) }
    var border by remember { mutableStateOf(borderColor) }
    var textValue by remember { mutableStateOf(value) }

    Column {
        if (showCharacterCount) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = "${textValue.length}",
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text = "/$maxNumberOfCharacters",
                    style = MaterialTheme.typography.body2
                )
            }
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(textFieldHeight)
        ) {
            val stateVertical = rememberScrollState()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .border(
                        width = 2.dp,
                        color = if (error) MaterialTheme.colors.error else border
                    )
                    .verticalScroll(stateVertical)
            ) {
                BasicTextField(
                    value = maxNumberOfCharacters?.let { value.take(maxNumberOfCharacters).apply { textValue = this } }
                        ?: value,
                    enabled = enabled,
                    onValueChange = onValueChange,
                    textStyle = textStyle,
                    modifier = Modifier
                        .onFocusChanged { focusState ->
                            hint = if (focusState.hasFocus) "" else hintText
                            border = if (focusState.hasFocus) focusedBorderColor else borderColor
                        }
                        .fillMaxSize(),
                    decorationBox = { innerTextField ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(backgroundColor)
                                .padding(vertical = 14.dp, horizontal = 16.dp)
                                .fillMaxSize()
                                .then(modifier)
                        ) {
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
                        }
                    }
                )
            }

            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(
                    scrollState = stateVertical
                ),
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.TopEnd)
            )
        }
    }
}
