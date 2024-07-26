package br.com.graest.retinografo.ui.screens.signup

import android.content.Context
import android.graphics.Bitmap
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.savedinstancestate.rememberSavedInstanceState
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.graest.retinografo.utils.ImageConvertingUtils.bitmapToByteArray
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

class SignUpViewModel : ViewModel(){

    private val _signUpState = MutableStateFlow(SignUpState())

    val signUpState: StateFlow<SignUpState> = _signUpState
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SignUpState())

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.SetCEP -> {
                _signUpState.update {
                    it.copy(
                        cep = event.cep
                    )
                }
            }
            is SignUpEvent.SetCPF -> {
                _signUpState.update {
                    it.copy(
                        cpf = event.cpf
                    )
                }
            }
            is SignUpEvent.SetCRM -> {
                _signUpState.update {
                    it.copy(
                        crm = event.crm
                    )
                }
            }
            is SignUpEvent.SetConfirmPassword -> {
                _signUpState.update {
                    it.copy(
                        confirmPassword = event.confirmPassword
                    )
                }
            }
            is SignUpEvent.SetEmail -> {
                _signUpState.update {
                    it.copy(
                        email = event.email
                    )
                }
            }
            is SignUpEvent.SetName -> {
                _signUpState.update {
                    it.copy(
                        name = event.name
                    )
                }
            }
            is SignUpEvent.SetPassword -> {
                _signUpState.update {
                    it.copy(
                        password = event.password
                    )
                }
            }
            is SignUpEvent.SetSurname -> {
                _signUpState.update {
                    it.copy(
                        surname = event.surname
                    )
                }
            }
            SignUpEvent.HideSignUpDialog -> {
                _signUpState.update {
                    it.copy(
                        showDialog = false
                    )
                }
            }

            SignUpEvent.ShowSignUpDialog -> {
                _signUpState.update {
                    it.copy(
                        showDialog = true
                    )
                }
            }
        }
    }
    fun imageAlreadyEdited(){
        _signUpState.update {
            it.copy(
                isEditingImage = false,
            )
        }
    }


    fun setCapturedImagePath(path: String?) {
        _signUpState.update {
            it.copy(
                tempImagePath = path
            )
        }
    }

    fun setErrorMessage(message: String?) {
        _signUpState.update {
            it.copy(
                tempImageErrorMessage = message
            )
        }
    }
}