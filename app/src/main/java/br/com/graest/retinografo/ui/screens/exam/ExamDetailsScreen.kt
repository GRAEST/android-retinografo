package br.com.graest.retinografo.ui.screens.exam

import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.graest.retinografo.R
import br.com.graest.retinografo.utils.FormatTime.calculateAge
import br.com.graest.retinografo.utils.ImageConvertingUtils.byteArrayToBitmap

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExamDetailsScreen(
    state: ExamDataState,
    onEvent: (ExamDataEvent) -> Unit
) {
    val scrollState = rememberScrollState()

    if (state.onShowImageDetail) {
        ExamDetailScreenDialog(
            state = state,
            onEvent = onEvent,
            modifier = Modifier
        )
    }
    Column (
        modifier = Modifier
            .padding(10.dp)
            .verticalScroll(scrollState)
    ) {
        PatientSelected(
            examDataState = state
        )
        Text(
            text = "Left Eye Images",
            fontSize = 22.sp,
            fontWeight = FontWeight(600),
            modifier = Modifier.padding(10.dp)

        )
        LazyHorizontalGrid(
            rows = GridCells.Fixed(1),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(10.dp)
        ) {
            state.examData?.let {
                itemsIndexed(it.listImagesLeftEye) { index, imagePath ->
                    val bitmap = BitmapFactory.decodeFile(imagePath)
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Captured Image",
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                            .aspectRatio(1f)
                            .clickable {
                                onEvent(ExamDataEvent.OnShowImageDetails(index, "left"))
                            }
                        ,
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }
        Text(
            text = "Right Eye Images",
            fontSize = 22.sp,
            fontWeight = FontWeight(600),
            modifier = Modifier.padding(10.dp)
        )
        LazyHorizontalGrid(
            rows = GridCells.Fixed(1),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(10.dp)
            ) {
            state.examData?.let {
                itemsIndexed(it.listImagesRightEye) { index, imagePath ->
                    val bitmap = BitmapFactory.decodeFile(imagePath)
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Captured Image",
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                            .aspectRatio(1f)
                            .clickable {
                                onEvent(ExamDataEvent.OnShowImageDetails(index, "right"))
                            }
                        ,
                        contentScale = ContentScale.Crop
                    )

                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun PatientSelected(
    examDataState: ExamDataState
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray)
            .padding(8.dp)

    ) {
        if (examDataState.patientData != null) {
            if (examDataState.patientData.profilePicturePath != null) {
                val bitmap = BitmapFactory.decodeFile(examDataState.patientData.profilePicturePath)
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.padding(10.dp))

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(4f)
        ) {
            if (examDataState.patientData != null) {
                Text(
                    text = examDataState.patientData.name,
                    fontSize = 20.sp
                )
            }
            if (examDataState.patientData != null) {
                Text(
                    text = "${calculateAge(examDataState.patientData.birthDate)} years",
                    fontSize = 12.sp
                )
            }
        }
    }
}