package br.com.sentinelapp.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.sentinelapp.R
import br.com.sentinelapp.core.manager.AppBarManagerTitle
import br.com.sentinelapp.ui.theme.InterFont

@Composable
fun SettingsScreen() {

    val ScreenTitle = stringResource(R.string.title_settings)

    LaunchedEffect(Unit) {
        AppBarManagerTitle.setTitle(ScreenTitle)
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.title_backup),
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = InterFont
            )
        )
    }

}

