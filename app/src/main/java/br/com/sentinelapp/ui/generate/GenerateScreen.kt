package br.com.sentinelapp.ui.generate


import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.sentinelapp.R
import br.com.sentinelapp.composeable.SentinelCheckbox
import br.com.sentinelapp.composeable.SentinelTextField
import br.com.sentinelapp.core.manager.AppBarManagerTitle
import br.com.sentinelapp.core.util.generatePassword
import br.com.sentinelapp.ui.theme.buttonText

@Composable
fun GenerateScreen() {
    val ScreenTitle = stringResource(R.string.title_generate)

    val context = LocalContext.current;
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    var includeNumber by remember { mutableStateOf(false) }
    var includeUpperLetter by remember { mutableStateOf(false) }
    var includeLowerLetter by remember { mutableStateOf(false) }
    var includeSpecialChars by remember { mutableStateOf(false) }

    var specialCharsAllowed by remember { mutableStateOf("") }

    var generatedPassword by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        AppBarManagerTitle.setTitle(ScreenTitle)
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.title_size),
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.label_size_password),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = sliderPosition.toInt().toString(),
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Slider(
                value = sliderPosition,
                onValueChange = {
                    sliderPosition = it
                },
                valueRange = 1f..32f,
                steps = 31,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.secondary,
                    activeTrackColor = MaterialTheme.colorScheme.secondary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.title_options),
            style = MaterialTheme.typography.labelMedium
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SentinelCheckbox(
                checked = includeNumber,
                label = R.string.label_include_number,
                onCheckedChange = { includeNumber = it }
            )
            SentinelCheckbox(
                checked = includeUpperLetter,
                label = R.string.label_include_upper_letter,
                onCheckedChange = { includeUpperLetter = it }
            )
            SentinelCheckbox(
                checked = includeLowerLetter,
                label = R.string.label_include_lowwercase_letter,
                onCheckedChange = { includeLowerLetter = it }
            )
            SentinelCheckbox(
                checked = includeSpecialChars,
                label = R.string.label_include_special_characters,
                onCheckedChange = { includeSpecialChars = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        SentinelTextField(
            value = specialCharsAllowed,
            onValueChange = { specialCharsAllowed = it },
            placeholder = R.string.label_specific_chars,
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true
        )



        Button(
            onClick = {

                generatePassword(
                    length = sliderPosition.toInt(),
                    includeUppercase = includeUpperLetter,
                    includeNumbers = includeNumber,
                    includeSpecialChars = includeSpecialChars,
                    mustContain = specialCharsAllowed
                )
                    .also { generatedPassword = it }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(size = 4.dp)
        ) {
            Text(
                text = stringResource(R.string.button_generate_password),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.buttonText,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }



        SentinelTextField(
            value = generatedPassword,
            onValueChange = { generatedPassword = it },
            placeholder = R.string.label_generated_password,
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true,
            trailingIcon = {
                val clipboardManager = LocalClipboardManager.current
                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(generatedPassword))
                        Toast.makeText(context, R.string.toast_password_copied, Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null)
                }
            }
        )


    }

}