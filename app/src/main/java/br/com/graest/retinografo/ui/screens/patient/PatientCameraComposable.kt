package br.com.graest.retinografo.ui.screens.patient

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.dp
import br.com.graest.retinografo.ui.components.CameraViewScreen
import br.com.graest.retinografo.utils.CameraUtils.takePhoto


@Composable
fun PatientCameraComposable(
    applicationContext: Context,
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CameraViewScreen(
            controller = controller,
            modifier = Modifier.fillMaxSize()
        )

        CircularOpeningOverlay(
            modifier = Modifier.fillMaxSize(),
            circleRadius = 100f, // Adjust the radius as needed
            circleCenter = Offset(300f, 300f), // Adjust the center as needed
            backgroundColor = Color.Gray.copy(alpha = 0.5f) // Semi-transparent background
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
            IconButton (
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
                    contentDescription = "Switch Camera"
                )
            }
            IconButton(
                onClick = {
                    takePhoto(
                        applicationContext = applicationContext,
                        controller = controller,
                        onPhotoTaken = onPhotoTaken
                    )
                },
                modifier = Modifier.offset(16.dp, 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = "Take Photo"
                )
            }
            IconButton(
                onClick = { },
                modifier = Modifier.offset(16.dp, 16.dp)
            ) {

            }
        }
    }
}

@Composable
fun CircularOpeningOverlay(
    modifier: Modifier = Modifier,
    circleRadius: Float,
    circleCenter: Offset,
    backgroundColor: Color
) {
    Canvas(modifier = modifier) {
        // Create a path for the full background
        val backgroundPath = Path().apply {
            addRect(Rect(0f, 0f, size.width, size.height))
        }
        // Draw the semi-transparent background
        drawPath(path = backgroundPath, color = backgroundColor)

        // Create a path for the circular opening
        val circlePath = Path().apply {
            addRoundRect(
                RoundRect(
                    Rect(
                        left = circleCenter.x - circleRadius,
                        top = circleCenter.y - circleRadius,
                        right = circleCenter.x + circleRadius,
                        bottom = circleCenter.y + circleRadius
                    ),
                    cornerRadius = CornerRadius(circleRadius, circleRadius)
                )
            )
        }

        // Clip the circular path to create the opening
        clipPath(circlePath) {
            drawRect(Color.Transparent)
        }
    }
}