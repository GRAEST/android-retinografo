package br.com.graest.retinografo.ui.screens.patient

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PatientScreen(
    state: PatientDataState,
    onEvent: (PatientDataEvent) -> Unit
) {

    if (state.isAddingPatientData || state.isEditingPatientData) {
        PatientDialog(state = state, onEvent = onEvent)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        items(state.patientsData) { patientData ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    if (patientData != null) {
                        Text(
                            text = patientData.name,
                            fontSize = 20.sp
                        )
                    }
                    if (patientData != null) {
                        Text(
                            text = "${patientData.age} anos",
                            fontSize = 12.sp
                        )
                    }
                }
                IconButton(onClick = {
                    if (patientData != null) {
                        onEvent(PatientDataEvent.ShowEditPatientDialog(patientData.id))
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Patient Data"
                    )
                }
            }

        }
    }

}

