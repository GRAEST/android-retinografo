package br.com.graest.retinografo.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.graest.retinografo.PatientDataEvent
import br.com.graest.retinografo.PatientDataState
import br.com.graest.retinografo.data.SortPatientType

@Composable
fun PatientScreen(
    state: PatientDataState,
    onEvent: (PatientDataEvent) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(PatientDataEvent.ShowDialog)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Contact"
                )
            }
        }
    ) { padding ->
        if (state.isAddingPatientData) {
            AddPatientDialog(state = state, onEvent = onEvent)
        }
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ) {
                    SortPatientType.entries.forEach { sortPatientType ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    onEvent(PatientDataEvent.SortPatientData(sortPatientType))
                                }
                        ) {
                            RadioButton(
                                selected = state.sortPatientDataType == sortPatientType,
                                onClick = {
                                    onEvent(PatientDataEvent.SortPatientData(sortPatientType))
                                }
                            )
                            Text(text = SortPatientType.entries.toString())
                        }

                    }
                }
            }
            items(state.patientData) { patientData ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = patientData.name,
                            fontSize = 20.sp
                        )
                        Text(
                            text = patientData.age,
                            fontSize = 12.sp
                        )
                    }
                    IconButton(onClick = {
                        onEvent(PatientDataEvent.DeletePatientData(patientData))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Patient Data"

                        )
                    }
                }

            }
        }
    }
}

