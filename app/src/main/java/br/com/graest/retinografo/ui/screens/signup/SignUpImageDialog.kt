package br.com.graest.retinografo.ui.screens.signup

import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import br.com.graest.retinografo.utils.ImageConvertingUtils.bitmapToByteArray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpImageDialog(
    applicationContext: Context,
    viewModel: SignUpViewModel,
    onEvent: (SignUpEvent) -> Unit
) {

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            viewModel.setPhoto(bitmap)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(applicationContext.contentResolver, uri)
            viewModel.setPhoto(bitmap)
        }
    }

    AlertDialog(
        onDismissRequest = {
            onEvent(SignUpEvent.HideSignUpDialog)
        },
        confirmButton = { },
        title = {
            Text("Choose an option")
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    onEvent(SignUpEvent.HideSignUpDialog)
                    cameraLauncher.launch(null)
                }) {
                    Text("Camera")
                }
                Button(onClick = {
                    onEvent(SignUpEvent.HideSignUpDialog)
                    galleryLauncher.launch("image/*")
                }) {
                    Text("Gallery")
                }
            }
        }
    )

}