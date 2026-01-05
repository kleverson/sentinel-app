package br.com.sentinelapp.ui.newpass

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.sentinelapp.composeable.SentinelTextField
import br.com.sentinelapp.core.manager.AppBarManagerTitle
import br.com.sentinelapp.R
import br.com.sentinelapp.core.navigation.BottomBarItens
import br.com.sentinelapp.ui.theme.buttonText
import br.com.sentinelapp.data.model.NewPasswordData


@Composable
fun NewPassScreen(
    navController: NavController,
    passwordId: String? = null,
    newPassViewModel: NewPassViewModel = hiltViewModel()
) {
    val ScreenTitle = stringResource(R.string.title_new_pass)

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()
    var data by remember { mutableStateOf(NewPasswordData()) }
    val newPasswordCreateState by newPassViewModel.newpasswordState.observeAsState()
    val currentPasswordState by newPassViewModel.loadedPassword.observeAsState()
    var isEdit by remember { mutableStateOf(false) }
    var showAlert by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        AppBarManagerTitle.setTitle(ScreenTitle)
    }

    LaunchedEffect(passwordId) {

        passwordId?.toIntOrNull()?.let { id ->
            isEdit = true
            Log.d("NEW_PASS_SCREEN", "Password ID: $passwordId")
            newPassViewModel.loadPassword(id)
        }
    }

    LaunchedEffect(currentPasswordState) {
        if (currentPasswordState is CurrentPasswordState.Success) {
            val item = (currentPasswordState as CurrentPasswordState.Success).passwordEntity
            data = NewPasswordData(
                accoutName = item?.user ?: "",
                providerName = item?.provider ?: "",
                password = item?.password ?: ""
            )
        }
    }



    when(newPasswordCreateState){
        is NewPasswordCreateState.Success -> {

            Toast.makeText(context, "Password created successfully", Toast.LENGTH_SHORT).show()
            navController.navigate(BottomBarItens.Home.route) {
                popUpTo(BottomBarItens.Home.route) {
                    inclusive = true
                }
            }
        }
        is NewPasswordCreateState.Error -> {
            Toast.makeText(context, "Password created error", Toast.LENGTH_SHORT).show()
        }
        else -> {
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        SentinelTextField(
            value = data.providerName,
            onValueChange = { value -> data = data.copy(providerName = value.trim())  },
            placeholder = R.string.label_provider_name,
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true,
            showLabel = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        SentinelTextField(
            value = data.accoutName,
            onValueChange = {  value -> data = data.copy(accoutName = value.trim())  },
            placeholder = R.string.label_account_name,
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true,
            showLabel = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        SentinelTextField(
            value = data.password,
            onValueChange = { value -> data = data.copy(password = value.trim()) },
            placeholder = R.string.label_new_password,
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true,
            showLabel = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    coroutineScope.launch { newPassViewModel.create(data) }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xff243647),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = when(isEdit){
                        true -> stringResource(R.string.button_save_changes)
                        false -> stringResource(R.string.button_create_password)
                    },
                    style = MaterialTheme.typography.buttonText,
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = { },
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xff243647),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.button_copy_password),
                    style = MaterialTheme.typography.buttonText,
                    textAlign = TextAlign.Center
                )
            }
        }



        if(isEdit){
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(
                    onClick = {
                        showAlert = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color(0xffffffff)
                    ),
                    shape = RoundedCornerShape(size = 4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.btn_exclude),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.buttonText,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }

    if(showAlert) {
        ConfirmRemove(
            title = stringResource(R.string.alert_remove_title),
            text = stringResource(R.string.alert_remove_text),
            onConfirm = {
                coroutineScope.launch {
                    newPassViewModel.delete(passwordId!!.toInt())
                    showAlert = false
                    navController.navigate(BottomBarItens.Home.route) {
                        popUpTo(BottomBarItens.Home.route) {
                            inclusive = true
                        }
                    }
                }
            },
            onDismiss = {
                showAlert = false
            }
        )
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
