package br.com.sentinelapp.composeable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
@Composable
fun SentinelTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: Int,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    showLabel: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = if (showLabel) {
            {
                Text(
                    text = stringResource(placeholder),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        } else null,
        placeholder = {
            Text(
                text = stringResource(placeholder),
                style = MaterialTheme.typography.labelSmall
            )
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,

        ),
        textStyle = MaterialTheme.typography.labelSmall,
        modifier = modifier,
        singleLine = singleLine,
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        enabled = !readOnly,
        readOnly = readOnly,
    )
}
