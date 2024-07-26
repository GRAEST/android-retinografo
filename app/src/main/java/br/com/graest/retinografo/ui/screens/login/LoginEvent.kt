package br.com.graest.retinografo.ui.screens.login

sealed interface LoginEvent {
    data class SetUserEmail(val userEmail: String) : LoginEvent
    data class SetUserPassword(val userPassword: String) : LoginEvent
}