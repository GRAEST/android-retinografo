package br.com.graest.retinografo.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import br.com.graest.retinografo.utils.ExamCameraUtils.takePhoto
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object PatientCameraUtils {
    val imageCapture = ImageCapture.Builder().build()

    fun captureImage(
        context: Context,
        controller: LifecycleCameraController,
        onImageCaptured: (File) -> Unit,
        onError: (Exception) -> Unit
    ) {
        takePhoto(context, controller) { bitmap ->
            val tempFile = createTempImageFile(context)
            if (saveBitmapToFile(bitmap, tempFile)) {
                onImageCaptured(tempFile)
            } else {
                onError(IOException("Failed to save image to file"))
            }
        }
    }

    fun createTempImageFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "temp_image", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }

    fun saveBitmapToFile(bitmap: Bitmap, file: File): Boolean {
        return try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun handleCancellation(tempFile: File) {
        if (tempFile.exists()) {
            tempFile.delete()
        }
    }

}