package br.com.graest.retinografo.ui.screens.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val requestMessage: String = "",
    val errorMessage: String = "",
    val successMessage: String = ""
)