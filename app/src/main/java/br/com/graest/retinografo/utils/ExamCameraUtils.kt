package br.com.graest.retinografo.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Environment
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream

object ExamCameraUtils {

    fun takePhoto(
        applicationContext: Context,
        controller: LifecycleCameraController,
        onPhotoTaken: (Bitmap) -> Unit,
    ) {
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

    //    fun saveImageToFile(context: Context, bitmap: Bitmap, fileName: String): String? {
//        return try {
//            val file = File(context.filesDir, fileName)
//            FileOutputStream(file).use { out ->
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
//            }
//            file.absolutePath
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
    fun createImageFile(context: Context, prefix: String = "image", suffix: String = ".jpg"): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            prefix, /* prefix */
            suffix, /* suffix */
            storageDir /* directory */
        )
    }
    fun saveImageToFile(context: Context, bitmap: Bitmap, fileName: String): String? {
        return try {
            val file = createImageFile(context, prefix = fileName)
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }



}