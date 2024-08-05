package br.com.graest.retinografo.ui.screens.exam

import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExamDetailScreenDialog(
    state: ExamDataState,
    onEvent: (ExamDataEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        modifier = modifier,
        title = {
            Text(text = "${state.imageDetailSide} - ${state.imageDetailIndex + 1}")
        },
        text = {
            val bitmap = if (state.imageDetailSide == "left"){
                BitmapFactory.decodeFile(state.examData!!.listImagesLeftEye[state.imageDetailIndex])
            } else {
                BitmapFactory.decodeFile(state.examData!!.listImagesRightEye[state.imageDetailIndex])
            }
            var offset by remember {
                mutableStateOf(Offset.Zero)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(true) {
                        detectDragGestures { change, _ ->
                            offset = change.position
                        }
                    }
                    .magnifier(
                        sourceCenter = {
                            offset
                        },
                        magnifierCenter = {
                            offset - Offset(x = 0f, y = 200f)
                        },
                        size = DpSize(200.dp, 200.dp),
                        cornerRadius = 200.dp
                    )
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
            }
        },
        onDismissRequest = {
            onEvent(ExamDataEvent.OnHideImageDetails)
        },
        confirmButton = {
            //not using a button
        })
}


