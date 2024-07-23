package br.com.graest.retinografo.ui.screens.signup

sealed interface SignUpEvent {
    data class SetEmail(val email: String): SignUpEvent
    data class SetPassword(val password: String): SignUpEvent
    data class SetConfirmPassword(val confirmPassword: String): SignUpEvent
    data class SetName(val name: String): SignUpEvent
    data class SetSurname(val surname: String): SignUpEvent
    data class SetCPF(val cpf: String): SignUpEvent
    data class SetCEP(val cep: String): SignUpEvent
    data class SetCRM(val crm: String): SignUpEvent
    data class SetPhoto(val photo: String): SignUpEvent
}