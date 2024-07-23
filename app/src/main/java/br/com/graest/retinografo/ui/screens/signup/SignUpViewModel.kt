package br.com.graest.retinografo.ui.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

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
            is SignUpEvent.SetPhoto -> {

            }
            is SignUpEvent.SetSurname -> {
                _signUpState.update {
                    it.copy(
                        surname = event.surname
                    )
                }
            }
        }
    }
}