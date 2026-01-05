package br.com.sentinelapp.composeable

import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PasswordValidationItem (
    isValid: Boolean,
    @StringRes text: Int
) {
    val isDark = isSystemInDarkTheme()


    val color = if (isValid)
       if (isDark) Color.White else MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.error

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            imageVector = if (isValid) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            modifier = Modifier.width(16.dp),
            tint = color

        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = stringResource(text), color = color, fontSize = 12.sp)
    }
}