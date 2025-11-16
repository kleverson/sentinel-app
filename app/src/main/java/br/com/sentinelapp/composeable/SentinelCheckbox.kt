package br.com.sentinelapp.composeable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.sentinelapp.R

@Composable
fun SentinelCheckbox(
    checked: Boolean,
    label: Int,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            stringResource(label),
            style = MaterialTheme.typography.labelSmall
        )
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}