package br.com.graest.retinografo.ui.screens.exam

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import br.com.graest.retinografo.data.model.ExamData
import br.com.graest.retinografo.utils.ImageConvertingUtils.byteArrayToBitmap


@Composable
fun ExamGridImages(
    state: ExamDataState,
) {


//    state.examsData.forEach { examData ->
//        examData?.image?.let { imageByteArray ->
//            bitmaps.add(byteArrayToBitmap(imageByteArray))
//        }
//    }

    if (state.examsData.isEmpty()) {
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
            items(state.examsData) { examData ->
                Row(

                ) {
                    if (examData != null) {
                        Image(
                            bitmap = byteArrayToBitmap(examData.image1).asImageBitmap(),
                            contentDescription = null,
                        )
                    }
                    Text(
                        text = "Name "
                    )
                }
            }
        }
    }
}