package br.com.graest.retinografo.data.remote.dto

import android.graphics.Bitmap
import kotlinx.serialization.Serializable

@Serializable
data class SignupDTO(
    val email: String,
    val password: String,
    val password2: String,
    val name: String,
    val surname: String,
    val cpf: String,
    val cep: String,
    val number: String,
    val crmList: List<String>,
    val image: ByteArray?
)
