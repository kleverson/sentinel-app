package br.com.sentinelapp.composeable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.sentinelapp.data.model.SnackBarData
import br.com.sentinelapp.data.model.SnackBarType
import br.com.sentinelapp.ui.theme.InterFont

@Composable
fun AnimatedSnackbarHost(
    hostState: SnackbarHostState
) {
    SnackbarHost(
        hostState = hostState
    ) { snackbarData ->
        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(
                initialOffsetY = { it }
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { it }
            ) + fadeOut()
        ) {
            SentinelAppSnackBar(
                data = SnackBarData(
                    message = snackbarData.visuals.message,
                    actionLabel = snackbarData.visuals.actionLabel,
                    onAction = { snackbarData.performAction() },
                    onDismiss = { snackbarData.dismiss() }
                )
            )
        }
    }
}


@Composable
fun SentinelAppSnackBar(
    data: SnackBarData
) {
    var bgColor = when (data.type) {
        SnackBarType.DEFAULT -> MaterialTheme.colorScheme.primaryContainer
        SnackBarType.SUCCESS -> Color.Green.copy(alpha = 0.6f)
        SnackBarType.ERROR -> Color.Red.copy(alpha = 0.6f)
        SnackBarType.WARNING -> Color.Yellow.copy(alpha = 0.6f)
        else -> MaterialTheme.colorScheme.primaryContainer
    }

    val contentColor =
        if (data.type == SnackBarType.DEFAULT) Color.Black else Color.White


    Snackbar(
        containerColor = bgColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = data.message,
                fontFamily = InterFont,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            data.actionLabel?.let { label ->
                TextButton(onClick = {
                    data.onAction?.invoke()
                    data.onDismiss?.invoke()
                }) {
                    Text(
                        text = label,
                        fontWeight = FontWeight.Bold,
                        color = contentColor
                    )
                }
            }
        }
    }


}