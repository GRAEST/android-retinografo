package br.com.graest.retinografo.ui

import androidx.camera.core.CameraControl
import androidx.camera.core.CameraInfo

data class FlashState(
    val isFlashOn : Boolean = false,
    val cameraControl: CameraControl? = null,
    val cameraInfo: CameraInfo? = null,
    val zoomRatio: Float = 1f,
)
