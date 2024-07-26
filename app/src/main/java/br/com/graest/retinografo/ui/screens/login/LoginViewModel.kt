package br.com.graest.retinografo.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

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

}