package br.com.graest.retinografo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun InitialScreenMain(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Olá! \uD83D\uDE03",
            fontSize = 32.sp,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Já tem uma conta?",
            fontSize = 24.sp,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.Start)
        )
        Button(
            onClick = { onLoginClick() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Criar uma conta?",
            fontSize = 24.sp,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.Start)
        )
        Button(
            onClick = { onSignUpClick() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }
        Spacer(modifier = Modifier.height(10.dp))

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInitialScreenMain() {
    InitialScreenMain(
        {},
        {}
    )
}