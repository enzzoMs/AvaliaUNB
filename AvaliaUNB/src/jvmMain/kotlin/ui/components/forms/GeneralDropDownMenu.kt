package ui.components.forms

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import theme.*

@Composable
fun GeneralDropDownMenu(
    menuItems: List<String>,
    selectedItem: String,
    onSelectItem: (String) -> Unit,
    dropDownMenuMinWidth: Dp = 300.dp,
    dropDownMenuMinHeight: Dp = 400.dp,
    selectedItemTextStyle: TextStyle = MaterialTheme.typography.subtitle2,
    dropDownTextStyle: TextStyle = MaterialTheme.typography.subtitle2,
    backgroundColor: Color = White,
    modifier: Modifier = Modifier
) {
    var expandedState by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = Gray),
            ) { expandedState = !expandedState }
            .padding(vertical = 8.dp, horizontal = 10.dp)
            .then(modifier)
    ) {
        Text(
            text = selectedItem,
            style = selectedItemTextStyle,
            maxLines = 1
        )

        DropdownMenu(
            expanded = expandedState,
            onDismissRequest = { expandedState = false }
        ) {
            Box(
                modifier = Modifier
                    .size(
                        width = dropDownMenuMinWidth,
                        height = dropDownMenuMinHeight
                    )
            ) {
                val dropDownListState = rememberLazyListState()

                LazyColumn(
                    state = dropDownListState
                ) {
                    items(menuItems) { item ->
                        DropdownMenuItem(
                            onClick = {
                                onSelectItem(item)
                                expandedState = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = item,
                                style = dropDownTextStyle
                            )
                        }
                    }
                }

                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(
                        scrollState = dropDownListState
                    )
                )
            }
        }

        Spacer(
            modifier = Modifier
                .weight(1f)
        )

        Icon(
            imageVector = Icons.Filled.ExpandMore,
            contentDescription = null,
            tint = DimGray,
            modifier = Modifier
                .padding(
                    end = 6.dp
                )
        )
    }
}