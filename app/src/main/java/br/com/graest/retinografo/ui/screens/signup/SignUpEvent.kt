package br.com.graest.retinografo.ui.screens.signup

import android.content.Context
import androidx.camera.view.LifecycleCameraController
import androidx.navigation.NavController
import java.io.File

sealed interface SignUpEvent {
    data class SetEmail(val email: String) : SignUpEvent
    data class SetPassword(val password: String) : SignUpEvent
    data class SetConfirmPassword(val confirmPassword: String) : SignUpEvent
    data class SetName(val name: String) : SignUpEvent
    data class SetSurname(val surname: String) : SignUpEvent
    data class SetCPF(val cpf: String) : SignUpEvent
    data class SetCEP(val cep: String) : SignUpEvent
    data class SetCRM(val crm: String) : SignUpEvent
    data object ShowSignUpDialog : SignUpEvent
    data object HideSignUpDialog : SignUpEvent
    data class OnTakePhoto(
        val context: Context,
        val controller: LifecycleCameraController,
        val navController: NavController,
        val onImageCaptured: (File) -> Unit,
        val onError: (Exception) -> Unit,
    ) : SignUpEvent
}