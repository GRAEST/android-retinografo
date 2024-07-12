package br.com.graest.retinografo.ui.screens.exam

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.graest.retinografo.ui.screens.patient.PatientDataState

@Composable
fun ExamDialog(
    patientDataState: PatientDataState,
    examDataState: ExamDataState,
    examDataViewModel: ExamDataViewModel,
    onEvent: (ExamDataEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    AlertDialog(
        modifier = modifier,
        title = {
            Text(text = "Add Patient")
        },
        text = {
            LazyColumn {
                items(patientDataState.patientsData) { patientData ->
                    if (patientData != null) {
                        Row {
                            Text(text = patientData.name)
                            Spacer(
                                modifier = Modifier.padding(10.dp)
                            )
                            Button(onClick = {
                                onEvent(ExamDataEvent.HideDialog)
                            }) {
                                Text(text = "Add")
                            }
                        }
                    }
                }
            }
        },
        onDismissRequest = {
            onEvent(ExamDataEvent.HideDialog)
        },
        confirmButton = {
            LazyColumn {

            }
        })
}