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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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

    val coroutineScope = rememberCoroutineScope()
    var data by remember { mutableStateOf(NewPasswordData()) }

    val passState by newPassViewModel.newpasswordState.observeAsState()

    LaunchedEffect(Unit) {
        AppBarManagerTitle.setTitle(ScreenTitle)
    }

    // Carrega os dados da senha se passwordId foi fornecido (modo edição)
    LaunchedEffect(passwordId) {
        passwordId?.toIntOrNull()?.let { id ->
            newPassViewModel.loadPassword(id)
        }
    }

    // Atualiza os campos quando os dados são carregados
    LaunchedEffect(newPassViewModel.loadedPassword.value) {
        Log.d("CURRENT_PASSWORD", "Loaded password: ${newPassViewModel.loadedPassword.value}")
        newPassViewModel.loadedPassword.value?.let { password ->
            data = NewPasswordData(
                accoutName = password.user,
                providerName = password.provider,
                password = password.password
            )
        }
    }

    val context = LocalContext.current

    when(passState){
        is NewPasswordCreateState.Success -> {

            Toast.makeText(context, "Password created successfully", Toast.LENGTH_SHORT).show()
            navController.navigate(BottomBarItens.Home.route) {
                popUpTo(BottomBarItens.Home.route) {
                    inclusive = true
                }
            }
            // Handle success state, e.g., show a message or navigate back
        }
        is NewPasswordCreateState.Error -> {
            Toast.makeText(context, "Password created error", Toast.LENGTH_SHORT).show()
            // Handle error state, e.g., show an error message
        }
        else -> {
            // Handle other states if necessary
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
                    text = stringResource(R.string.button_edit_password),
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

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(
                onClick = {


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
