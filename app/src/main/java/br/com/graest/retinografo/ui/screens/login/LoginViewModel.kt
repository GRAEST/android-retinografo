package br.com.graest.retinografo.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.graest.retinografo.data.remote.RequestSender
import br.com.graest.retinografo.data.remote.dto.LoginDTO
import br.com.graest.retinografo.data.remote.util.Result
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

class LoginViewModel : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())

    val loginState: StateFlow<LoginState> = _loginState
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LoginState())

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.SetUserEmail -> {
                _loginState.update {
                    it.copy(
                        email = event.userEmail
                    )
                }
            }

            is LoginEvent.SetUserPassword -> {
                _loginState.update {
                    it.copy(
                        password = event.userPassword
                    )
                }
            }
            is LoginEvent.SendLoginRequest -> {
                viewModelScope.launch {
                    val loginDTO = LoginDTO(event.email, event.password)
                    when (val result = event.requestSender.sendLoginInfo(loginDTO)) {
                        is Result.Error -> {
                            _loginState.update {
                                it.copy(
                                    errorMessage = result.error.toString()
                                )
                            }
                        }
                        is Result.Success -> {
                            _loginState.update {
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
}