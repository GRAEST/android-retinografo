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
import androidx.core.content.FileProvider
import br.com.graest.retinografo.utils.ImageConvertingUtils.bitmapToByteArray
import java.util.Objects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpImageDialog(
    onLaunchCamera: () -> Unit,
    onEvent: (SignUpEvent) -> Unit
) {
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
                    onLaunchCamera()
                    onEvent(SignUpEvent.HideSignUpDialog)
                }) {
                    Text("Camera")
                }
                //Work In Progress
//                Button(onClick = {
//                    onEvent(SignUpEvent.HideSignUpDialog)
//                    galleryLauncher.launch("image/*")
//                }) {
//                    Text("Gallery")
//                }
            }
        }
    )
}