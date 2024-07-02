package br.com.graest.retinografo.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.graest.retinografo.R

@Composable
fun EditUserData() {

    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("") }
    var mainEmail by remember { mutableStateOf("") }
    var secondaryEmail by remember { mutableStateOf("") }
    var crm by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }


    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(15.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier.padding(10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier
                    .size(250.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
            )
            Row {
                IconButton(
                    onClick = {
                        //choose between camera and photos
                    },
                    modifier = Modifier.size(60.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.camera),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
            }
        }

        Spacer(modifier = Modifier.padding(10.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(10.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text(text = "Name") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Row {
                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    placeholder = { Text(text = "Age") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Right) }
                    ),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.padding(4.dp))
                OutlinedTextField(
                    value = sex,
                    onValueChange = { sex = it },
                    placeholder = { Text(text = "Sex") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }
            OutlinedTextField(
                value = mainEmail,
                onValueChange = { mainEmail = it },
                placeholder = { Text(text = "Main Email") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = secondaryEmail,
                onValueChange = { secondaryEmail = it },
                placeholder = { Text(text = "Secondary Email") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = crm,
                onValueChange = { crm = it },
                placeholder = { Text(text = "CRM") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                placeholder = { Text(text = "Phone Number") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }


        Spacer(modifier = Modifier.padding(10.dp))

        Button(onClick = {
            //Save Patient
        }) {
            Text(
                text = "Save",
                fontSize = 18.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEditUserData() {
    EditUserData()
}