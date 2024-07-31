package br.com.graest.retinografo.ui.screens.exam

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
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
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import br.com.graest.retinografo.R
import br.com.graest.retinografo.data.model.PatientData
import br.com.graest.retinografo.ui.components.CameraViewScreen
import br.com.graest.retinografo.ui.screens.patient.PatientDataState
import br.com.graest.retinografo.utils.FormatTime.calculateAge
import br.com.graest.retinografo.utils.CameraUtils.captureImage

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

    LaunchedEffect(Unit) {
        controller.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        examDataViewModel.setCameraController(controller)
    }

    LaunchedEffect(examDataState.setFlash) {

    }

    val maxZoomRatio = examDataState.cameraInfo?.zoomState?.value?.maxZoomRatio ?: 1f
    val minZoomRatio = examDataState.cameraInfo?.zoomState?.value?.minZoomRatio ?: 1f

    if (examDataState.showToastRed) {
        RedToast(examDataState)
    }
    if (examDataState.showToastGreen) {
        GreenToast(examDataState)
    }

    if (examDataState.showAddPatientDialog) {
        ExamAddPatientDialog(
            patientDataState = patientDataState,
            onEvent = onEvent
        )
    }

    if (examDataState.readyToSave) {
        ExamAddLocationDialog(
            examDataState = examDataState,
            onEvent = onEvent,
            applicationContext = applicationContext
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {

        if (!examDataState.patientSelected) {
            AddPatientToExamButton(onEvent)
        }
        if (examDataState.patientSelected) {
            Column {
                TopCameraComposableButtons(examDataState, onEvent)
                examDataState.patientData?.let {
                    TopCameraComposablePatientSelected(
                        examDataState,
                        it, onEvent
                    )
                }
            }
        }


        MainCameraComposable(controller = controller)

        BottomCameraComposable(
            examDataState,
            applicationContext,
            controller,
            navController,
            examDataViewModel,
            onEvent,
            minZoomRatio,
            maxZoomRatio
        )
    }
}

@Composable
private fun GreenToast(examDataState: ExamDataState) {
    Popup(alignment = Alignment.TopCenter) {
        Box(
            modifier = Modifier
                .padding(top = 50.dp)
                .background(Color.Green, shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text(
                text = examDataState.greenToastMessage,
                color = Color.White,
                fontWeight = FontWeight(800)
            )
        }
    }
}

@Composable
private fun RedToast(examDataState: ExamDataState) {
    Popup(alignment = Alignment.TopCenter) {
        Box(
            modifier = Modifier
                .padding(top = 50.dp)
                .background(Color.Red, shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text(
                text = examDataState.redToastMessage,
                color = Color.White,
                fontWeight = FontWeight(800)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun TopCameraComposablePatientSelected(
    examDataState: ExamDataState,
    patientData: PatientData,
    onEvent: (ExamDataEvent) -> Unit,
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
                val bitmap = BitmapFactory.decodeFile(patientData.profilePicturePath)
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
                    text = patientData.name,
                    fontSize = 20.sp
                )
            }
            if (examDataState.patientData != null) {
                Text(
                    text = "${calculateAge(patientData.birthDate)} years",
                    fontSize = 12.sp
                )
            }
        }
        IconButton(
            onClick = {
                onEvent(ExamDataEvent.NoPatientSelected)
                onEvent(ExamDataEvent.OnCancelExam)
                onEvent(ExamDataEvent.OnShowToastRed("Exam Cancelled"))
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

@Composable
private fun TopCameraComposableButtons(
    examDataState: ExamDataState,
    onEvent: (ExamDataEvent) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp
            )
            .clip(RoundedCornerShape(16.dp))
            .background(Color.DarkGray)
            .padding(8.dp)
    ) {
        if (examDataState.onRightEyeSaveMode) {
            Column(
                modifier = Modifier.weight(3f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Saving Right Eye Image",
                    fontSize = 22.sp,
                    fontWeight = FontWeight(800),
                    color = Color.White
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "R-${examDataState.rightEyeImagePaths.size}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight(800),
                        color = Color.White

                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    Button(onClick = {
                        onEvent(ExamDataEvent.OnLeftEyeSaveMode)
                    }) {
                        Text(text = "Capture Left Eye")
                    }
                }
            }
        }
        if (examDataState.onLeftEyeSaveMode) {
            Column(
                modifier = Modifier.weight(3f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Saving Left Eye Image",
                    fontSize = 22.sp,
                    fontWeight = FontWeight(800),
                    color = Color.White
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "L-${examDataState.leftEyeImagePaths.size}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight(800),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    Button(onClick = {
                        onEvent(ExamDataEvent.OnRightEyeSaveMode)
                    }) {
                        Text(text = "Capture Right Eye")
                    }
                }
            }
        }
        Button(
            onClick = {
                if (examDataState.leftEyeImagePaths.isNotEmpty() && examDataState.rightEyeImagePaths.isNotEmpty()) {
                    onEvent(ExamDataEvent.OnReadyToSave)
                }
            },
            modifier = Modifier
                .weight(1f),
            colors = ButtonDefaults.buttonColors(Color.Green)
        ) {
            Text("Finish")
        }
    }
}

@Composable
private fun AddPatientToExamButton(onEvent: (ExamDataEvent) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
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

@Composable
private fun BottomCameraComposable(
    examDataState: ExamDataState,
    applicationContext: Context,
    controller: LifecycleCameraController,
    navController: NavController,
    examDataViewModel: ExamDataViewModel,
    onEvent: (ExamDataEvent) -> Unit,
    minZoomRatio: Float,
    maxZoomRatio: Float,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,

            ) {


            Switch(
                checked = examDataState.setFlash,
                onCheckedChange = {
                    onEvent(ExamDataEvent.SetFlash(it))
                },
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = {
                    if (examDataState.patientSelected) {
                        if (examDataState.onRightEyeSaveMode) {
                            captureImage(
                                context = applicationContext,
                                controller = controller,
                                navController = navController,
                                onImageCaptured = { file ->
                                    examDataViewModel.addRightEyeImagePath(
                                        path = file.absolutePath
                                    )
                                    examDataViewModel.setErrorMessage(null)
                                    //começar contagem e dar algum sinal de tela carregando
                                },
                                onError = { error ->
                                    examDataViewModel.setErrorMessage(error.message)
                                }
                            )
                        }
                        if (examDataState.onLeftEyeSaveMode) {
                            captureImage(
                                context = applicationContext,
                                controller = controller,
                                navController = navController,
                                onImageCaptured = { file ->
                                    examDataViewModel.addLeftEyeImagePath(
                                        path = file.absolutePath
                                    )
                                    examDataViewModel.setErrorMessage(null)
                                    //dar algum sinal de tela carregando
                                },
                                onError = { error ->
                                    examDataViewModel.setErrorMessage(error.message)
                                }
                            )
                        }
                    } else {
                        onEvent(ExamDataEvent.OnShowToastRed("First Select a Patient"))
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    tint = Color.White,
                    contentDescription = "Take Photo"
                )
            }

            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .graphicsLayer(rotationZ = -90f)
                    .weight(1f)
            ) {
                Slider(
                    value = examDataState.zoomRatio,
                    onValueChange = {
                        Log.d("CameraZoom", "Slider value changed to: ${examDataState.zoomRatio}")
                        onEvent(ExamDataEvent.SetZoom(it))
                    },
                    valueRange = minZoomRatio..maxZoomRatio,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun MainCameraComposable(
    controller: LifecycleCameraController,
) {
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
}

