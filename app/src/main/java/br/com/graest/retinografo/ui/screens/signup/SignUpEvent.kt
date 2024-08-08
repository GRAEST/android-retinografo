package br.com.graest.retinografo.ui.screens.signup

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.view.LifecycleCameraController
import androidx.navigation.NavController
import br.com.graest.retinografo.data.remote.RequestSender
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
    data class SendLoginRequest(
        val requestSender: RequestSender,
        val email: String,
        val password: String,
        val confirmPassword: String,
        val name: String,
        val surname: String,
        val cpf: String,
        val cep: String,
        val number: String = "00000",
        val crmList: List<String>,
        val image: Bitmap?
    ): SignUpEvent
}