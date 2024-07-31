package br.com.graest.retinografo.ui

import android.app.Application
import android.util.Log
import androidx.camera.view.LifecycleCameraController
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FlashViewModel(application: Application) : AndroidViewModel(application) {

    private val _flashState = MutableStateFlow(FlashState())

    val flashState: StateFlow<FlashState> = _flashState
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FlashState())

    fun onEvent(event: FlashEvent){
        when (event) {
            is FlashEvent.OnSetFlash -> {
                _flashState.update {
                    it.copy(
                        isFlashOn = event.flashOn
                    )
                }
            }
            is FlashEvent.SetZoom -> {
                viewModelScope.launch {
                    _flashState.update {
                        it.copy(
                            zoomRatio = event.newValue
                        )
                    }
                    _flashState.value.cameraControl?.setZoomRatio(event.newValue) ?: run {
                    }
                }
            }
        }
    }

    fun setCameraController(controller: LifecycleCameraController) {
        Log.d("CameraZoom", "Setting camera controller.")
        val cameraControl = controller.cameraControl
        val cameraInfo = controller.cameraInfo

        if (cameraControl == null || cameraInfo == null) {
            Log.e("CameraZoom", "CameraControl or CameraInfo is null.")
            return
        }

        _flashState.update {
            it.copy(
                cameraControl = cameraControl,
                cameraInfo = cameraInfo
            )
        }
    }

}