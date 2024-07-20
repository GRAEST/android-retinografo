package br.com.graest.retinografo.ui.screens.exam

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import br.com.graest.retinografo.R
import br.com.graest.retinografo.ui.components.CameraViewScreen
import br.com.graest.retinografo.ui.screens.patient.PatientDataState
import br.com.graest.retinografo.utils.FormatTime.calculateAge
import br.com.graest.retinografo.utils.ImageConvertingUtils.byteArrayToBitmap
import br.com.graest.retinografo.utils.PatientCameraUtils.captureImage

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExamCameraComposableScreen(
    patientDataState: PatientDataState,
    examDataState: ExamDataState,
    examDataViewModel: ExamDataViewModel,
    onEvent: (ExamDataEvent) -> Unit,
    applicationContext: Context,
    controller: LifecycleCameraController,
    navController: NavController,
) {

    val capturedImagePaths by examDataViewModel.capturedImagePaths.collectAsState()

    LaunchedEffect(Unit) {
        controller.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    }

    if (examDataState.showToastRed) {
        Popup(alignment = Alignment.TopCenter) {
            Box(
                modifier = Modifier
                    .padding(top = 50.dp)
                    .background(Color.Red, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "First Select a Patient!",
                    color = Color.White,
                    fontWeight = FontWeight(800)
                )
            }
        }
    }
    if (examDataState.showToastGreen) {
        Popup(alignment = Alignment.TopCenter) {
            Box(
                modifier = Modifier
                    .padding(top = 50.dp)
                    .background(Color.Green, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "Exam Saved!",
                    color = Color.White,
                    fontWeight = FontWeight(800)
                )
            }
        }
    }

    if (examDataState.showAddPatientDialog) {
        ExamAddPatientDialog(
            patientDataState = patientDataState,
            onEvent = onEvent
        )
    }

    if (capturedImagePaths.size == 4) {
        ExamAddLocationDialog(
            examDataState = examDataState,
            onEvent = onEvent,
            applicationContext = applicationContext
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        if (!examDataState.patientSelected) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {

                Button(
                    onClick = {
                        onEvent(ExamDataEvent.ShowAddPatientDialog)
                    },
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                            .aspectRatio(1f),
                    )

                    Spacer(modifier = Modifier.padding(10.dp))

                    Column(
                        Modifier.weight(4f)
                    ) {
                        Text(
                            text = "Add Patient",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight(600)
                        )
                    }
                }
            }
        }
        if (examDataState.patientSelected) {

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
                    byteArrayToBitmap(examDataState.patientData.profilePicture)?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .weight(1f)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                                .aspectRatio(1f),
                            contentScale = ContentScale.Crop
                        )
                    }
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
                            text = "${calculateAge(examDataState.patientData.birthDate)} anos",
                            fontSize = 12.sp
                        )
                    }
                }

                IconButton(
                    onClick = {
                        onEvent(ExamDataEvent.NoPatientSelected)
                        onEvent(ExamDataEvent.OnCancelExam)
                    },
                    colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.error)
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "Cancel Button"
                    )
                }

            }

        }
        Box(
            contentAlignment = Alignment.Center
        ) {
            CameraViewScreen(
                controller = controller,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RectangleShape)
                    .aspectRatio(1f)
            )
            Image(
                painter = painterResource(id = R.drawable.cameraoverlay),
                contentDescription = "Camera Overlay"
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "${capturedImagePaths.size}",
                color = Color.White
            )


            IconButton(
                onClick = {
                    if (examDataState.patientSelected) {
                        captureImage(
                            context = applicationContext,
                            controller = controller,
                            navController = navController,
                            onImageCaptured = { file ->
                                examDataViewModel.addImagePath(
                                    path = file.absolutePath
                                )
                                examDataViewModel.setErrorMessage(null)
                                //começar contagem e dar algum sinal de tela carregando
                            },
                            onError = { error ->
                                examDataViewModel.setErrorMessage(error.message)
                            }
                        )
                    } else {
                        onEvent(ExamDataEvent.OnShowToastRed)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    tint = Color.White,
                    contentDescription = "Take Photo"
                )
            }

            Text(
                text = "${capturedImagePaths.size}",
                color = Color.White
                //isso junto com um when consegue o comportamento do figma (D-1) (D-2) (E-1) (E-2)
            )
        }
    }
}

