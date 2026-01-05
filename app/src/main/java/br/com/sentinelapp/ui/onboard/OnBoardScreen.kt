package br.com.sentinelapp.ui.onboard

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import br.com.sentinelapp.core.manager.AppBarManagerTitle
import br.com.sentinelapp.R
import br.com.sentinelapp.composeable.PasswordValidationItem
import br.com.sentinelapp.composeable.SentinelTextField
import br.com.sentinelapp.core.manager.SnackBarManager
import br.com.sentinelapp.data.model.NewMasterPassword
import br.com.sentinelapp.data.model.SnackBarType
import br.com.sentinelapp.ui.theme.buttonText
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardScreen(
    @Suppress("UNUSED_PARAMETER") navController: NavController,
    onBoardViewModel: OnBoardViewModel = hiltViewModel()
) {

    val coroutine = rememberCoroutineScope()
    AppBarManagerTitle.setTitle(stringResource(R.string.onboard_title))

    var masterPassword by remember { mutableStateOf(NewMasterPassword(
        password = "",
        confirmPassword = ""
    )) }

    val sucessMessage = stringResource(R.string.onboard_master_password_created)



    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val term = stringResource(R.string.onboard_welcome)
        Text(
            text = term,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        val description = buildAnnotatedString {
            append(stringResource(R.string.pre_description))
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(stringResource(R.string.pre_description_highlight))
            }
            append(stringResource(R.string.description_for_you))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(description, fontWeight = FontWeight.Normal, fontSize = 16.sp)

        Spacer(modifier = Modifier.height(8.dp))

        SentinelTextField(
            value = masterPassword.password,
            onValueChange = {
                masterPassword = masterPassword.copy(password = it)
            },
            placeholder = R.string.onboard_password,
            modifier = Modifier.fillMaxWidth(),
            showLabel = true,
        )

        Spacer(modifier = Modifier.height(8.dp))

        SentinelTextField(
            value = masterPassword.confirmPassword,
            onValueChange = {
                masterPassword = masterPassword.copy(confirmPassword = it)
            },
            placeholder = R.string.confirm_password,
            modifier = Modifier.fillMaxWidth(),
            showLabel = true,
        )


        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.White, RoundedCornerShape(8.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            PasswordValidationItem(
                masterPassword.hasMinLength(8),
                R.string.validation_min_length
            )
            PasswordValidationItem(
                masterPassword.hasUppercase(),
                R.string.validation_uppercase_letter
            )
            PasswordValidationItem(
                masterPassword.hasNumber(),
                R.string.validation_has_number
            )
            PasswordValidationItem(
                masterPassword.hasSpecialChar(),
                R.string.special_char
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            enabled = masterPassword.isValid(8),
            onClick = {
                Log.d("OnBoardScreen", "=== SAVE BUTTON CLICKED ===")
                Log.d("OnBoardScreen", "Password valid: ${masterPassword.isValid(8)}")
                Log.d("OnBoardScreen", "Password length: ${masterPassword.password.length}")

                coroutine.launch {
                    try{
                        Log.d("OnBoardScreen", "Inside coroutine launch...")
                        if(masterPassword.isValid(8)) {
                            Log.d("OnBoardScreen", "Validation passed, calling createMasterPassword...")
                            // Await suspend function
                            onBoardViewModel.createMasterPassword(masterPassword.password)

                            Log.d("OnBoardScreen", "createMasterPassword returned successfully")

                            // Use the manager API to show success
                            SnackBarManager.show(
                                message = sucessMessage,
                                type = SnackBarType.SUCCESS
                            )
                            Log.d("OnBoardScreen", "Success snackbar shown")
                        } else {
                            Log.w("OnBoardScreen", "Password validation failed inside coroutine")
                        }
                    }catch (ex: Exception){
                        Log.e("OnBoardScreen", "=== ERROR in onClick coroutine ===", ex)
                        Log.e("OnBoardScreen", "Error creating master password", ex)
                        SnackBarManager.show(
                            message = "Erro ao salvar senha: ${ex.message ?: "Desconhecido"}",
                            type = SnackBarType.DEFAULT
                        )
                    }

                }
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.button_save_changes),
                    style = MaterialTheme.typography.buttonText,
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}