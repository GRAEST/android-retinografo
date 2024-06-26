package br.com.graest.retinografo.ui.screens.patient

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.graest.retinografo.R

@Composable
fun PatientScreen(
    state: PatientDataState,
    onEvent: (PatientDataEvent) -> Unit,
    onLaunchCamera: () -> Unit
) {

    if (state.isAddingPatientData || state.isEditingPatientData) {
        PatientDialog(state = state, onEvent = onEvent, onLaunchCamera = onLaunchCamera)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        items(state.patientsData) { patientData ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                )

                Spacer(modifier = Modifier.padding(10.dp))

                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(4f)
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

