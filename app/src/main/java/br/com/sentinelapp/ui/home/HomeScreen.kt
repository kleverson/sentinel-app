package br.com.sentinelapp.ui.home


import android.app.AlertDialog
import android.util.Log
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
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import br.com.sentinelapp.core.manager.AppBarManagerTitle
import br.com.sentinelapp.R
import br.com.sentinelapp.composeable.SentinelTextField
import br.com.sentinelapp.core.data.entities.PasswordEntity
import br.com.sentinelapp.core.navigation.BottomBarItens
import br.com.sentinelapp.ui.theme.ListItemSubTitle
import br.com.sentinelapp.ui.theme.ListItemTitle
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
) {
    val ScreenTitle = stringResource(R.string.title_home)

    var searchTerm by remember { mutableStateOf("") }


    val context = LocalContext.current

    val passwordStates by homeScreenViewModel.passwordState.observeAsState()

    LaunchedEffect(Unit) {
        AppBarManagerTitle.setTitle(ScreenTitle)
    }

    LaunchedEffect(searchTerm) {
        if(searchTerm != null && searchTerm.length > 0){
            homeScreenViewModel.searchPassword(searchTerm)
        } else {
            homeScreenViewModel.loadPasswords()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when (passwordStates) {
                is PasswordListState.Loading -> {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.padding(8.dp))
                            Text("Carregando senhas...")
                        }
                    }
                }

                is PasswordListState.Error -> {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = (passwordStates as PasswordListState.Error).message,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                is PasswordListState.Success -> {
                    val itens = (passwordStates as PasswordListState.Success).data

                    Log.d("HomeScreen", "Passwords loaded: ${itens.size} content=>${itens}")

                    if (itens.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Nenhuma senha cadastrada",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.padding(8.dp))
                                Text(
                                    text = "Adicione uma nova senha usando o botão + na parte inferior",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }

                    itemsIndexed(itens) { index, item ->
                        listItem(item, navController)
                    }
                }

                null -> {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Carregando...")
                        }
                    }
                }
            }
        }
    }


}


@Composable
fun listItem(item: PasswordEntity,navController: NavController, homeScreenViewModel: HomeScreenViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val coroutineScope = rememberCoroutineScope()


    var showDialog by remember { mutableStateOf(false) }

    fun onCloseDialog() {
        showDialog = false
    }

    fun onConfirmRemove() {
        coroutineScope.launch {
            homeScreenViewModel.removeItem(item)
            showDialog = false
        }
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .combinedClickable(
                onClick = {
                    Log.d("CURRENT_ID", "id => ${item.id}")
                    navController.navigate(BottomBarItens.NewPass.createRoute(item.id)) {
                        launchSingleTop = true
                    }
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
                text = item.provider,
                style = MaterialTheme.typography.ListItemTitle,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = item.user,
                style = MaterialTheme.typography.ListItemSubTitle,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun ConfirmRemove(title: String, text: String, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = {
            // Called when the user dismisses the dialog (e.g., taps outside or presses back)
            onDismiss()
        },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = text)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    // Handle the dismissal action
                    onDismiss()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}