package br.com.graest.retinografo.ui.screens.exam

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.graest.retinografo.ui.screens.patient.PatientDataState

@Composable
fun ExamDialog(
    patientDataState: PatientDataState,
    onEvent: (ExamDataEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    AlertDialog(
        modifier = modifier,
        title = {
            Text(text = "Add Patient")
        },
        text = {
            if (patientDataState.patientsData.isEmpty()) {
                Box(
                    modifier = Modifier.padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("There are no patients registered yet")
                }
            } else {
                LazyColumn {
                    items(patientDataState.patientsData) { patientData ->
                        if (patientData != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(10.dp)
                            ) {
                                Column(
                                    modifier = Modifier.weight(4f)
                                ) {
                                    Text(
                                        text = patientData.name,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight(700)
                                    )
                                    Text(
                                        text = "${patientData.age.toString()} years old",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight(500)
                                    )
                                }
                                Spacer(
                                    modifier = Modifier.padding(10.dp)
                                )
                                IconButton(
                                    onClick = {
                                        onEvent(ExamDataEvent.PatientSelected(patientData))
                                        onEvent(ExamDataEvent.HideDialog)
                                    },
                                    colors = IconButtonDefaults.iconButtonColors(Color.Gray),
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add",
                                    )
                                }
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
            //not using a button because it could get inaccessible as the patient number grows
        })
}