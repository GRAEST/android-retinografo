package br.com.graest.retinografo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.graest.retinografo.PatientDataEvent
import br.com.graest.retinografo.PatientDataState

@Composable
fun AddPatientDialog(
    state: PatientDataState,
    onEvent: (PatientDataEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        title = { Text(text = "Add Patient") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.name,
                    onValueChange = {
                        onEvent(PatientDataEvent.SetPatientName(it))
                    },
                    placeholder = {
                        Text(text = "Name")
                    }
                )
                TextField(
                    value = state.age,
                    onValueChange = {
                        onEvent(PatientDataEvent.SetPatientAge(it))
                    },
                    placeholder = {
                        Text(text = "Age")
                    }
                )
            }
        },
        onDismissRequest = {
            onEvent(PatientDataEvent.HideDialog)
        },
        confirmButton = {
            Box (
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(onClick = { onEvent(PatientDataEvent.SavePatientData) }) {
                    Text(text = "Add")
                }
            }
        })
}