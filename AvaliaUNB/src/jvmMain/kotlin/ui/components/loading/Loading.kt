package ui.components.loading

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import theme.LightGray
import utils.resources.ResourcesUtils

@Composable
fun Loading() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 80.dp)
    ) {
        CircularProgressIndicator(
            color = LightGray,
            strokeWidth = 3.dp,
            modifier = Modifier
                .size(80.dp)
        )
        Text(
            text = ResourcesUtils.Strings.LOADING,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier
                .padding(top = 16.dp)
        )
    }
}