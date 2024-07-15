package br.com.graest.retinografo.ui.screens.exam

import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import br.com.graest.retinografo.utils.FormatTime.formatExamTime


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExamList(
    examState: ExamDataState,
) {

    if (examState.examsDataWithPatient.isEmpty()) {
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("There are no exams yet")
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            items(examState.examsDataWithPatient) { examDataWithPatient ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (examDataWithPatient != null) {
                        val bitmap = BitmapFactory.decodeFile(examDataWithPatient.examData.imagePath1)
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Captured Image",
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                                .aspectRatio(1f)
                                .weight(1f)
                            ,
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    if (examDataWithPatient != null) {
                        Column(
                            modifier = Modifier.weight(4f)
                        ) {
                            Text(
                                text = examDataWithPatient.patientData.name,

                            )
                            Text(
                                text = formatExamTime(examDataWithPatient.examData.examTime),
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(2f)
                    ) {
                        if (examDataWithPatient != null) {
                            Text(text = examDataWithPatient.examData.examLocation)
                        }
                    }
                }
            }
        }
    }
}