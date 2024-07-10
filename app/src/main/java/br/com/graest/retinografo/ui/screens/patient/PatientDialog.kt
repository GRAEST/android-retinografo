package br.com.graest.retinografo.ui.screens.patient

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import br.com.graest.retinografo.ui.screens.exam.ExamDataViewModel

@Composable
fun PatientDialog(
    state: PatientDataState,
    onEvent: (PatientDataEvent) -> Unit,
    onLaunchCamera: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PatientDataViewModel
) {
    val capturedImagePath = viewModel.capturedImagePath.collectAsState()

    AlertDialog(
        modifier = modifier,
        title = {
            if (state.isAddingPatientData) {
                Text(text = "Add Patient")
            }
            if (state.isEditingPatientData) {
                Text(text = "Editing Patient")
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (capturedImagePath.value != null) {
                    val bitmap = BitmapFactory.decodeFile(capturedImagePath.value)
                    Image(bitmap = bitmap.asImageBitmap(), contentDescription = "Captured Image")
                } else {
                    Text(text = "No image captured yet.")
                }

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
                Button(onClick = {
                    onLaunchCamera()
                }) {
                    if (state.isAddingPatientData) {
                        Text(text = "Add photo")
                    }
                    if (state.isEditingPatientData) {
                        Text(text = "Edit photo")
                    }
                }
            }
        },
        onDismissRequest = {
            onEvent(PatientDataEvent.HideDialog)
        },
        confirmButton = {
            if (state.isAddingPatientData) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = {
                        onEvent(PatientDataEvent.SavePatientData)
                    }) {
                        Text(text = "Add")
                    }
                }
            }
            if (state.isEditingPatientData) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                onEvent(PatientDataEvent.HideDialog)
                            },
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                        ) {
                            Text(text = "Cancel", color = MaterialTheme.colorScheme.onError)
                        }
                        Spacer(modifier = Modifier.width(8.dp))

                        Button(onClick = {
                            onEvent(PatientDataEvent.SavePatientData)
                        }) {
                            Text(text = "Save")
                        }
                    }
                    IconButton(onClick = {
                        //verificar se tem diferença em usar um deles, se sim, deletar o defeituoso
                        //onEvent(PatientDataEvent.DeletePatientDataById(state.id))
                        onEvent(PatientDataEvent.DeletePatientData)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Patient"
                        )
                    }
                }
            }
        })
}