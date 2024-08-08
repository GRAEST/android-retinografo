package br.com.graest.retinografo.ui.screens.signup

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.graest.retinografo.R

@Composable
fun SignUpScreenA(
    signUpState: SignUpState,
    onEvent: (SignUpEvent) -> Unit,
    onClickNext: () -> Unit,
    onLaunchCamera: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    if (signUpState.showDialog) {
        SignUpImageDialog(
            onEvent = onEvent,
            onLaunchCamera = onLaunchCamera
        )
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
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

            if (signUpState.imagePath != null) {
                Spacer(modifier = Modifier.padding(10.dp))
                val bitmap = BitmapFactory.decodeFile(signUpState.imagePath)
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Captured Image",
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                            .aspectRatio(1f)
                            .weight(1f),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    Column(
                        modifier = Modifier.weight(2f),
                        verticalArrangement = Arrangement.Bottom,
                    ) {
                        Button(onClick = { onEvent(SignUpEvent.ShowSignUpDialog) }) {
                            Text("Change Photo")
                        }
                        Text(
                            text = "Photo Added",
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(10.dp))
            } else {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = {
                        onEvent(SignUpEvent.ShowSignUpDialog)
                    }) {
                        Text("Add Photo")
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    Text(text = "No Photo Added")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row {
                Column(modifier = Modifier.weight(1f)) {}
                Button(
                    onClick = { onClickNext() },
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
