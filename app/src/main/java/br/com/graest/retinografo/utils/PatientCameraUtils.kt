package br.com.graest.retinografo.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import androidx.camera.view.LifecycleCameraController
import androidx.navigation.NavController
import br.com.graest.retinografo.getCurrentRoute
import br.com.graest.retinografo.utils.ExamCameraUtils.takePhoto
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object PatientCameraUtils {

    fun captureImage(
        context: Context,
        controller: LifecycleCameraController,
        navController: NavController,
        onImageCaptured: (File) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val currentRoute = navController.currentDestination?.route

        takePhoto(context, controller) { bitmap ->
            val imageFile = createImageFile(context)
            if (saveBitmapToFile(bitmap, imageFile)) {
                onImageCaptured(imageFile)
                if (currentRoute == "PatientCamera" || currentRoute == "UserCamera") {
                    navController.popBackStack()
                }
            } else {
                onError(IOException("Failed to save image to file"))
            }
        }
    }

    private fun createImageFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFileName = "image_${System.currentTimeMillis()}.jpg"
        return File(storageDir, imageFileName)
    }

    private fun saveBitmapToFile(bitmap: Bitmap, file: File): Boolean {
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

    fun deleteImageFile(file: File): Boolean {
        return file.delete()
    }

    fun overwriteImageFile(bitmap: Bitmap, file: File): Boolean {
        return try {
            if (file.exists()) {
                file.delete()
            }
            saveBitmapToFile(bitmap, file)
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

}