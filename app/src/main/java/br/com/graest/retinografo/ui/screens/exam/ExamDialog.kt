package br.com.graest.retinografo.ui.screens.exam

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import br.com.graest.retinografo.ui.screens.patient.PatientDataEvent
import br.com.graest.retinografo.utils.ImageConvertingUtils.byteArrayToBitmap

@Composable
fun ExamDialog(
    state: ExamDataState,
    onEvent: (ExamDataEvent) -> Unit,
    onLaunchCamera: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExamDataViewModel
) {

    AlertDialog(
        modifier = modifier,
        title = {
            Text(text = "Add Patient")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "List Patients")
            }
        },
        onDismissRequest = {
            
        },
        confirmButton = {
            LazyColumn {

            }
        })
}