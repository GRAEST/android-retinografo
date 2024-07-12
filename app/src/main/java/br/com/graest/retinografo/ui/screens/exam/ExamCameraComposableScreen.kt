package br.com.graest.retinografo.ui.screens.exam

import android.content.Context
import android.graphics.Bitmap
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import br.com.graest.retinografo.R
import br.com.graest.retinografo.ui.components.CameraViewScreen
import br.com.graest.retinografo.utils.ExamCameraUtils.takePhoto
import br.com.graest.retinografo.utils.ShapeUtils

@Composable
fun ExamCameraComposableScreen(
    applicationContext: Context,
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit
) {
    LaunchedEffect(Unit) {
        controller.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        val hexagon = remember {
            RoundedPolygon(
                6,
                rounding = CornerRounding(0.2f)
            )
        }
        val clip = remember(hexagon) {
            ShapeUtils.RoundedPolygonShape(polygon = hexagon)
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Button(
                onClick = {},
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

                Column (
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


        CameraViewScreen(
            controller = controller,
            modifier = Modifier
                .fillMaxSize()
                .clip(clip)
                .aspectRatio(1f)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            IconButton(
                onClick = {
                    takePhoto(
                        applicationContext = applicationContext,
                        controller = controller,
                        onPhotoTaken = onPhotoTaken
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    tint = Color.White,
                    contentDescription = "Take Photo"
                )
            }
        }
    }
}

