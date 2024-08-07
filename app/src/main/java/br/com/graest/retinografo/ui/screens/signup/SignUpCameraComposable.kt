package br.com.graest.retinografo.ui.screens.signup

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.graest.retinografo.ui.components.CameraViewScreen
import br.com.graest.retinografo.utils.CameraUtils.captureImage


@Composable
fun SignUpCameraComposable(
    applicationContext: Context,
    controller: LifecycleCameraController,
    navController: NavController,
    viewModel: SignUpViewModel,
    onEvent: (SignUpEvent) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        CameraViewScreen(
            controller = controller,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    clip = true
                    shape = CircleShape
                }
                .aspectRatio(1f)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 48.dp
                ),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(
                onClick = {
                    controller.cameraSelector =
                        if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                            CameraSelector.DEFAULT_FRONT_CAMERA
                        } else CameraSelector.DEFAULT_BACK_CAMERA
                },
                modifier = Modifier.offset(16.dp, 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    tint = Color.White,
                    contentDescription = "Switch Camera"
                )
            }
            IconButton(
                onClick = {
                    onEvent(SignUpEvent.OnTakePhoto(
                        context = applicationContext,
                        controller = controller,
                        navController = navController,
                        onImageCaptured = { file ->
                            viewModel.setCapturedImagePath(file.absolutePath)
                            viewModel.setErrorMessage(null)
                            viewModel.imageAlreadyEdited()
                        },
                        onError = { error ->
                            viewModel.setErrorMessage(error.message)
                            viewModel.setCapturedImagePath(null)
                        }
                    ))
                },
                modifier = Modifier.offset(16.dp, 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    tint = Color.White,
                    contentDescription = "Take Photo"
                )
            }
            IconButton(
                onClick = { },
                modifier = Modifier.offset(16.dp, 16.dp)
            ) {
                //fake button -> just to make it symmetrical
            }
        }
    }
}
