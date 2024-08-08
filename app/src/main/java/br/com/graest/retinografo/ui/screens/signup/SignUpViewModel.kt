package br.com.graest.retinografo.ui.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.graest.retinografo.data.remote.dto.SignupDTO
import br.com.graest.retinografo.data.remote.util.Result
import br.com.graest.retinografo.utils.CameraUtils.captureImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

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
            is SignUpEvent.OnTakePhoto -> {
                viewModelScope.launch {
                    captureImage(
                        event.context,
                        event.controller,
                        event.navController,
                        event.onImageCaptured,
                        event.onError
                    )
                }
            }
            is SignUpEvent.SendLoginRequest -> {
                viewModelScope.launch {
                    val signupDTO = SignupDTO(
                        email = event.email,
                        password = event.password,
                        password2 = event.confirmPassword,
                        name = event.name,
                        surname = event.surname,
                        cpf = event.cpf,
                        cep = event.cep,
                        number = event.number,
                        crmList = event.crmList,
                        image = event.image
                    )
                    when (val result = event.requestSender.sendSignupInfo(signupDTO)) {
                        is Result.Error -> {
                            _signUpState.update {
                                it.copy(
                                    errorMessage = result.error.toString()
                                )
                            }
                        }
                        is Result.Success -> {
                            _signUpState.update {
                                it.copy(
                                    successMessage = result.data
                                )
                            }
                        }
                    }
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
                imagePath = path
            )
        }
    }

    fun setErrorMessage(message: String?) {
        _signUpState.update {
            it.copy(
                imageErrorMessage = message
            )
        }
    }

}