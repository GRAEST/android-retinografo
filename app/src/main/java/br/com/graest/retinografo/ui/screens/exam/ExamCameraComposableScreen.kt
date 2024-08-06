package br.com.graest.retinografo.ui.screens.exam

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import br.com.graest.retinografo.R
import br.com.graest.retinografo.data.model.PatientData
import br.com.graest.retinografo.ui.FlashEvent
import br.com.graest.retinografo.ui.FlashState
import br.com.graest.retinografo.ui.FlashViewModel
import br.com.graest.retinografo.ui.components.CameraViewScreen
import br.com.graest.retinografo.ui.screens.patient.PatientDataState
import br.com.graest.retinografo.utils.CameraUtils.takePhoto
import br.com.graest.retinografo.utils.FormatTime.calculateAge

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
    flashViewModel: FlashViewModel,
    flashState: FlashState,
    onFlashEvent: (FlashEvent) -> Unit,
    onNavigateToDetails: () -> Unit
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE
            )
            bindToLifecycle(lifecycleOwner)
        }
    }


    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                onEvent(ExamDataEvent.OnSetNavigationStatusToFalse)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            cameraController.unbind()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(cameraController) {
        flashViewModel.setCameraController(cameraController)
    }

    LaunchedEffect(Unit) {
        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        onEvent(ExamDataEvent.OnSetNavigationStatusToFalse)
    }

    LaunchedEffect(flashState.isFlashOn) {
        cameraController.enableTorch(flashState.isFlashOn)
    }

    LaunchedEffect(examDataState.onNavigateToDetails) {
        if (examDataState.onNavigateToDetails) {
            onNavigateToDetails()

        }
    }


    val maxZoomRatio = flashState.cameraInfo?.zoomState?.value?.maxZoomRatio ?: 4f
    val minZoomRatio = flashState.cameraInfo?.zoomState?.value?.minZoomRatio ?: 1f
    Log.d("CameraZoom", "minZoomRatio: $minZoomRatio, maxZoomRatio: $maxZoomRatio")


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
            SelectPatientToExamButton(onEvent)
        }
        if (examDataState.patientSelected) {
            examDataState.patientData?.let {
                TopCameraComposable(examDataState = examDataState, patientData = it, onEvent)
            }

        }

        MainCameraComposable(controller = cameraController)

        BottomCameraComposable(
            examDataState,
            applicationContext,
            cameraController,
            navController,
            examDataViewModel,
            onEvent,
            minZoomRatio,
            maxZoomRatio,
            flashViewModel,
            flashState,
            onFlashEvent,
            cameraController
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
private fun TopCameraComposable(
    examDataState: ExamDataState,
    patientData: PatientData,
    onEvent: (ExamDataEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.DarkGray)
            .padding(8.dp)
    ) {
        Row {
            if (examDataState.patientData != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(4f)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = patientData.name,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Text(
                            text = "${calculateAge(patientData.birthDate)} years",
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                    IconButton(
                        onClick = {
                            onEvent(ExamDataEvent.NoPatientSelected)
                            onEvent(ExamDataEvent.OnCancelExam)
                            onEvent(ExamDataEvent.OnShowToastRed("Exam Finished"))
                        },
                        colors = IconButtonDefaults.iconButtonColors(Color.Red)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "Cancel Button"
                        )
                    }
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                if (examDataState.onLeftEyeSaveMode) {
                    Text(
                        text = "Left Eye",
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }
                if (examDataState.onRightEyeSaveMode) {
                    Text(
                        text = "Right Eye",
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.padding(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "OS(${examDataState.leftEyeBitmaps.size})",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Switch(
                        checked = examDataState.onRightEyeSaveMode,
                        onCheckedChange = {
                            if (examDataState.onRightEyeSaveMode) {
                                onEvent(ExamDataEvent.OnLeftEyeSaveMode)
                            }
                            if (examDataState.onLeftEyeSaveMode) {
                                onEvent(ExamDataEvent.OnRightEyeSaveMode)
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedBorderColor = Color.Black,
                            uncheckedBorderColor = Color.Black,
                            checkedIconColor = Color.Black,
                            uncheckedIconColor = Color.Black,
                            checkedThumbColor = Color.Yellow,
                            uncheckedThumbColor = Color.Yellow,
                            checkedTrackColor = Color.White,
                            uncheckedTrackColor = Color.White,
                        ),
                        thumbContent = {
                            if (examDataState.onLeftEyeSaveMode) {
                                Icon(imageVector = Icons.Default.ArrowCircleLeft, contentDescription = "arrow left")
                            }
                            if (examDataState.onRightEyeSaveMode) {
                                Icon(imageVector = Icons.Default.ArrowCircleRight, contentDescription = "arrow right")
                            }
                        }
                    )
                    Text(
                        text = "OD(${examDataState.rightEyeBitmaps.size})",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
            IconButton(
                onClick = {
                    if (examDataState.leftEyeBitmaps.isNotEmpty() && examDataState.rightEyeBitmaps.isNotEmpty()) {
                        onEvent(ExamDataEvent.OnReadyToSave)
                    }
                },
                modifier = Modifier,
                colors = IconButtonDefaults.iconButtonColors(Color.Green)
            ) {
                Icon(imageVector = Icons.Default.SaveAlt, contentDescription = "Save Exam")
            }
        }
    }
}

@Composable
private fun SelectPatientToExamButton(onEvent: (ExamDataEvent) -> Unit) {
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
                    text = "Select Patient",
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
    flashViewModel: FlashViewModel,
    flashState: FlashState,
    onFlashEvent: (FlashEvent) -> Unit,
    cameraController: LifecycleCameraController
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
                checked = flashState.isFlashOn,
                onCheckedChange = {
                    onFlashEvent(FlashEvent.OnSetFlash(it))
                },
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = {
                    if (examDataState.patientSelected) {
                        if (examDataState.onRightEyeSaveMode) {
                            onEvent(ExamDataEvent.OnTakePhoto(
                                applicationContext = applicationContext,
                                cameraController = cameraController,
                                onPhotoTaken = examDataViewModel::onTakeRightEyePhoto
                            ))
//                            takePhoto(
//                                applicationContext = applicationContext,
//                                controller = cameraController,
//                                onPhotoTaken = examDataViewModel::onTakeRightEyePhoto
//                            )
                        }
                        if (examDataState.onLeftEyeSaveMode) {
                            onEvent(ExamDataEvent.OnTakePhoto(
                                applicationContext = applicationContext,
                                cameraController = cameraController,
                                onPhotoTaken = examDataViewModel::onTakeLeftEyePhoto
                            ))
//                            takePhoto(
//                                applicationContext = applicationContext,
//                                controller = cameraController,
//                                onPhotoTaken = examDataViewModel::onTakeLeftEyePhoto
//                            )
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
                    value = flashState.zoomRatio,
                    onValueChange = {
                        Log.d("CameraZoom", "Slider value changed to: ${flashState.zoomRatio}")
                        onFlashEvent(FlashEvent.SetZoom(it))
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
        contentAlignment = Alignment.Center,
    ) {
        CameraViewScreen(
            controller = controller,
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RectangleShape)
                .aspectRatio(1f)
                .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))

        )
        Image(
            painter = painterResource(id = R.drawable.cameraoverlay),
            contentDescription = "Camera Overlay"
        )
    }
}

