package br.com.graest.retinografo.ui.screens.signup

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.graest.retinografo.R
import br.com.graest.retinografo.utils.ImageConvertingUtils.bitmapToByteArray
import br.com.graest.retinografo.utils.ImageConvertingUtils.byteArrayToBitmap

@Composable
fun SignUpScreenA(
    viewModel: SignUpViewModel,
    applicationContext: Context,
    signUpState: SignUpState,
    onEvent: (SignUpEvent) -> Unit,
    onClickSignUp: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    if(signUpState.showDialog) {
        SignUpImageDialog(
            applicationContext = applicationContext,
            viewModel = viewModel,
            onEvent = onEvent
        )
    }

    Column(
        modifier = Modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Image(
            painter = painterResource(id = R.drawable.foxconn),
            contentDescription = null,
            modifier = Modifier
                .width(300.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Personal Data",
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.Start)
            )

            OutlinedTextField(
                value = signUpState.name,
                onValueChange = {
                    onEvent(SignUpEvent.SetName(it))
                },
                label = { Text("Name") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = signUpState.surname,
                onValueChange = {
                    onEvent(SignUpEvent.SetSurname(it))
                },
                label = { Text("Surname") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = signUpState.cpf,
                onValueChange = {
                    onEvent(SignUpEvent.SetCPF(it))
                },
                label = { Text("CPF") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = signUpState.cep,
                onValueChange = {
                    onEvent(SignUpEvent.SetCEP(it))
                },
                label = { Text("CEP") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = signUpState.crm,
                onValueChange = {
                    onEvent(SignUpEvent.SetCRM(it))
                },
                label = { Text("CRM") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            if (signUpState.photo != null){
                Image(
                    bitmap = byteArrayToBitmap(signUpState.photo).asImageBitmap(),
                    contentDescription = "User Photo"
                )
            }
            
            Text(text = "${signUpState.photo}")
            Button(onClick = {
                onEvent(SignUpEvent.ShowSignUpDialog)
            }) {
                Text("Add Photo")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row {
                Column(modifier = Modifier.weight(1f)) {}
                Button(
                    onClick = { onClickSignUp() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(3f)
                ) {
                    Text("Next")
                }
                Column(modifier = Modifier.weight(1f)) {}
            }

        }
    }

}
