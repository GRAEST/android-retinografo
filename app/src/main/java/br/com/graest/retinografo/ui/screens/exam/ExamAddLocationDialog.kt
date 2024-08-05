package br.com.graest.retinografo.ui.screens.exam

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamAddLocationDialog(
    examDataState: ExamDataState,
    onEvent: (ExamDataEvent) -> Unit,
    applicationContext: Context,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        title = {
            if (examDataState.isLocationAdded) {
                Text(
                    text = "Save Exam"
                )
            } else {
                Text(
                    text = "Add Exam Location")
            }

        },
        onDismissRequest = {
            //this dialog cannot be closed after opened
        },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (examDataState.isLocationAdded) {
                    Button(
                        onClick = {
                            onEvent(ExamDataEvent.SaveExamData(applicationContext))
                            onEvent(ExamDataEvent.OnShowToastGreen("Exam Saved Successfully!"))
                        },
                        colors = ButtonDefaults.buttonColors(Color.Green)
                    ) {
                        Text(text = "Save Exam")
                    }
                } else {
                    Button(onClick = {
                        onEvent(ExamDataEvent.SetIsLocationAddedTrue)
                    }) {
                        Text(text = "Save Location")

                    }
                }
            }
        },
        text = {
            if (examDataState.isLocationAdded) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Left Eye: ${examDataState.leftEyeBitmaps.size} images captured")
                    Text(text = "Right Eye: ${examDataState.rightEyeBitmaps.size} images captured")
                    Text(text = "Location: ${examDataState.examLocation}")
                }
            } else {
                TextField(
                    value = examDataState.examLocation,
                    onValueChange = {
                        onEvent(ExamDataEvent.SetExamLocation(it))
                    },
                    placeholder = {
                        Text(text = "Exam Location")
                    }
                )
            }
        }
    )
}
