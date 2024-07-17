package br.com.graest.retinografo.ui.screens.exam

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamAddLocationDialog(
    examDataState: ExamDataState,
    onEvent: (ExamDataEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = Modifier,
        title = {
            Text(text = "Add Exam Location")
        },
        onDismissRequest = {
            //onEvent(ExamDataEvent.HideAddLocationDialog)
        },
        confirmButton = {
            // posso enviar uma confirmacao para permitir salvar exame
            // isso envolve alterar o botao salvar para abrir o dialog para adicionar o local
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    onEvent(ExamDataEvent.SetIsLocationAddedTrue)
                }) {
                    Text(text = "Save Location")
                }
            }
        },
        text = {
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
    )
}
