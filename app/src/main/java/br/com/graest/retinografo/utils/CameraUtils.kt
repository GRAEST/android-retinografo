package br.com.graest.retinografo.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Environment
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object CameraUtils {


    suspend fun takePhoto(
        applicationContext: Context,
        controller: LifecycleCameraController,
        onPhotoTaken: (Bitmap) -> Unit,
    ) {
        return withContext(Dispatchers.Main){
            controller.takePicture(
                ContextCompat.getMainExecutor(applicationContext),
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        super.onCaptureSuccess(image)

                        val matrix = Matrix().apply {
                            postRotate(image.imageInfo.rotationDegrees.toFloat())
                        }
                        val rotatedBitmap = Bitmap.createBitmap(
                            image.toBitmap(),
                            0,
                            0,
                            image.width,
                            image.height,
                            matrix,
                            true
                        )
                        val removedGreen = BitmapUtils.removeGreen(rotatedBitmap)

                        onPhotoTaken(rotatedBitmap)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        super.onError(exception)
                        Log.e("Camera", "Couldn't take photo: ", exception)
                    }

                }
            )
        }
    }

    suspend fun saveBitmapToExternalStorage(context: Context, bitmap: Bitmap, fileName: String): String? {
        return withContext(Dispatchers.IO) {
            val externalStorageDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            if (externalStorageDirectory != null) {
                val file = File(externalStorageDirectory, "$fileName.jpg")
                try {
                    FileOutputStream(file).use { out ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                    }
                    return@withContext file.absolutePath
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return@withContext null
        }
    }


    suspend fun captureImage(
        context: Context,
        controller: LifecycleCameraController,
        navController: NavController,
        onImageCaptured: (File) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val currentRoute = navController.currentDestination?.route

        takePhoto(context, controller) { bitmap ->
            saveImageAsync(context, bitmap, currentRoute, navController, onImageCaptured, onError)
        }
    }

    suspend fun createImageFile(context: Context): File {
        return withContext(Dispatchers.IO) {
            val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val imageFileName = "image_${System.currentTimeMillis()}.jpg"
            return@withContext File(storageDir, imageFileName)
        }
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

    fun generateBitmapFromFile(filePath: String): Bitmap? {
        return BitmapFactory.decodeFile(filePath)
    }

    private fun saveImageAsync(
        context: Context,
        bitmap: Bitmap,
        currentRoute: String?,
        navController: NavController,
        onImageCaptured: (File) -> Unit,
        onError: (Exception) -> Unit
    ) {
        kotlinx.coroutines.CoroutineScope(Dispatchers.Main).launch {
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