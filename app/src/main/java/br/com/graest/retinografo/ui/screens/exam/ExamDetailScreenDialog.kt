package br.com.graest.retinografo.ui.screens.exam

import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import br.com.graest.retinografo.utils.ImageConvertingUtils.byteArrayToBitmap


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExamDetailScreentDialog(
    state: ExamDataState,
    onEvent: (ExamDataEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    AlertDialog(
        modifier = modifier,
        title = {
            Text(text =  "${state.imageDetailIndex}")
        },
        text = {
            val bitmap = BitmapFactory.decodeFile(state.examData!!.listImagesRightEye[state.imageDetailIndex])
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
        },
        onDismissRequest = {
            //onEvent(ExamDataEvent.HideAddPatientDialog)
            //change state and viewmodel
        },
        confirmButton = {
            //not using a button because it could get inaccessible as the patient number grows
        })
}


