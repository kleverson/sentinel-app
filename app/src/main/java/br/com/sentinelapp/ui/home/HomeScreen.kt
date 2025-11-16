package br.com.sentinelapp.ui.home


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import br.com.sentinelapp.core.manager.AppBarManagerTitle
import br.com.sentinelapp.R
import br.com.sentinelapp.composeable.SentinelTextField
import br.com.sentinelapp.data.mock.generateTestPasswords
import br.com.sentinelapp.data.model.PasswordItens
import br.com.sentinelapp.ui.theme.ListItemSubTitle
import br.com.sentinelapp.ui.theme.ListItemTitle

@Composable
fun HomeScreen() {
    val ScreenTitle = stringResource(R.string.title_home)

    var searchTerm by remember { mutableStateOf("") }

    val listPasswords: List<PasswordItens> = generateTestPasswords()

    val clipboardManager = LocalClipboardManager.current

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        AppBarManagerTitle.setTitle(ScreenTitle)
    }


    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)) {
        item {
            SentinelTextField(
                value = searchTerm,
                onValueChange = { searchTerm = it },
                placeholder = R.string.label_search,
                modifier = Modifier
                    .fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                }
            )
        }

        itemsIndexed(listPasswords) { index, item ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp).combinedClickable(
                    onClick = {
                        // Handle item click
                    },
                    onLongClick = {
                        clipboardManager.setText(AnnotatedString(item.password))
                        Toast.makeText(context, R.string.toast_password_copied, Toast.LENGTH_SHORT).show()

                        // Handle item long click
                    }
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.background(MaterialTheme.colorScheme.tertiary)
                ) {
                    Icon(
                        Icons.Default.Key,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column() {
                    Text(
                        text = item.user,
                        style = MaterialTheme.typography.ListItemTitle,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = item.provider,
                        style = MaterialTheme.typography.ListItemSubTitle,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }

    }

}