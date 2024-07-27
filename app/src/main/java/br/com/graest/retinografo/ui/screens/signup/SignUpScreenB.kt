package br.com.graest.retinografo.ui.screens.signup

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.graest.retinografo.R

@Composable
fun SignUpScreenB(
    viewModel: SignUpViewModel,
    signUpState: SignUpState,
    onEvent: (SignUpEvent) -> Unit,
    onClickSignUp: () -> Unit,
) {

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

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
                value = signUpState.email,
                onValueChange = {
                    onEvent(SignUpEvent.SetEmail(it))
                },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
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
                value = signUpState.password,
                onValueChange = {
                    onEvent(SignUpEvent.SetPassword(it))
                },
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
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
                value = signUpState.confirmPassword,
                onValueChange = {
                    onEvent(SignUpEvent.SetConfirmPassword(it))
                },
                label = { Text("Confirm Password") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
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


            Spacer(modifier = Modifier.height(10.dp))

            Row {
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        viewModel.sendSignUpInfo(
                            name = signUpState.name,
                            surname = signUpState.surname,
                            cpf = signUpState.cpf,
                            cep = signUpState.cep,
                            crmList = signUpState.crmList,
                            tempImagePath = signUpState.tempImagePath ?: "",
                            email = signUpState.email,
                            password = signUpState.password
                        )
                        onClickSignUp()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(3f)
                ) {
                    Text("Next")
                }
                Spacer(modifier = Modifier.weight(1f))
            }

        }
    }
}
