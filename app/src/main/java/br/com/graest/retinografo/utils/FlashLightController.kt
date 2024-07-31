package br.com.graest.retinografo.utils

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager

class FlashLightController(context: Context) {

    private val cameraManager: CameraManager =
        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private val cameraId: String = cameraManager.cameraIdList.firstOrNull()
        ?: throw IllegalStateException("No camera found")

    fun turnOn() {
        try {
            cameraManager.setTorchMode(cameraId, true)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    fun turnOff() {
        try {
            cameraManager.setTorchMode(cameraId, false)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }
}