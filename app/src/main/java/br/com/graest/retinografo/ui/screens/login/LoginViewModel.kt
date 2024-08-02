package br.com.graest.retinografo.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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
        }
    }

    fun sendLoginInfo(email: String, password: String) {
        val client = OkHttpClient()

        val jsonObject = JSONObject().apply {
            put("email", email)
            put("password", password)
        }
        val jsonString = jsonObject.toString()

        val requestBody = jsonString.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("https://76e79302a360.ngrok.app/api/account/login/")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            //aqui vai receber algo de volta da request (sucesso ou erro)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    _loginState.update {
                        it.copy(
                            requestMessage = response.code.toString()
                        )
                    }

                } else {
                    _loginState.update {
                        it.copy(
                            requestMessage = response.code.toString()
                        )
                    }
                }
            }
        })
    }

}