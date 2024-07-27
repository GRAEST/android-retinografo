package br.com.graest.retinografo.ui.screens.signup

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

    fun sendSignUpInfo(
        name: String,
        surname: String,
        cpf: String,
        cep: String,
        crmList: List<String>,
        tempImagePath: String,
        email: String,
        password: String,
    ) {
        val client = OkHttpClient()

        val jsonObject = JSONObject().apply {
            put("name", name)
            put("surname", surname)
            put("cpf", cpf)
            put("cep", cep)
            put("crmList", crmList)
            put("tempImagePath", tempImagePath)
            put("email", email)
            put("password", password)
            put("confirm_password", password)
        }
        val jsonString = jsonObject.toString()

        val requestBody = jsonString.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("https://b220878beb13.ngrok.app/api/account/login/")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            //aqui vai receber algo de volta da request (sucesso ou erro)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    _signUpState.update {
                        it.copy(
                            requestMessage = responseBody.toString()
                        )
                    }

                } else {
                    _signUpState.update {
                        it.copy(
                            requestMessage = response.code.toString()
                        )
                    }
                }
            }
        })
    }
}