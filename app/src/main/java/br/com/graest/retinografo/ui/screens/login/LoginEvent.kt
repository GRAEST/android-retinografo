package br.com.graest.retinografo.ui.screens.login

import br.com.graest.retinografo.data.remote.RequestSender

sealed interface LoginEvent {
    data class SetUserEmail(val userEmail: String) : LoginEvent
    data class SetUserPassword(val userPassword: String) : LoginEvent
    data class SendLoginRequest(
        val requestSender: RequestSender,
        val email: String,
        val password: String
    ): LoginEvent
}