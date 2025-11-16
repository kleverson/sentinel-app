package br.com.sentinelapp.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import br.com.sentinelapp.R
import br.com.sentinelapp.core.manager.AppBarManagerTitle

@Composable
fun NewPassScreen() {

    val ScreenTitle = stringResource(R.string.title_new_pass)

    LaunchedEffect(Unit) {
        AppBarManagerTitle.setTitle(ScreenTitle)
    }
    Column() {
        Text("settings")
    }

}