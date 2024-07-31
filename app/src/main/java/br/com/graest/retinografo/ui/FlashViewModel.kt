package br.com.graest.retinografo.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.graest.retinografo.ui.screens.signup.SignUpState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

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
        }
    }

//    fun toggleFlash() {
//        _flashState.value.isFlashOn = !_flashState.value.isFlashOn
//    }
}