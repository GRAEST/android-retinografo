package br.com.graest.retinografo.data.remote.dto

import android.graphics.Bitmap
import br.com.graest.retinografo.utils.BitmapSerializer
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
    @Serializable(with = BitmapSerializer::class)
    val image: Bitmap?
)
