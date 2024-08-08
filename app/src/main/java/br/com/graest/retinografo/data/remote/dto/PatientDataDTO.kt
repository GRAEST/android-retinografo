package br.com.graest.retinografo.data.remote.dto

import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.graest.retinografo.data.model.Gender
import br.com.graest.retinografo.utils.Converters
import br.com.graest.retinografo.utils.LocalDateSerializer
import br.com.graest.retinografo.utils.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class PatientDataDTO (
    val patientId: ByteArray,
    @Serializable(with = LocalDateTimeSerializer::class)
    @TypeConverters(Converters::class) val dataCreated: LocalDateTime,
    val profilePicture: ByteArray?,
    val name: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthDate: LocalDate? = null,
    val gender: Gender = Gender.OTHER,
    val cpf: String = "",
    val email: String = "",
    val telNumber: String = "",
    val isDiabetic: Boolean = false,
    val hasHyperTension: Boolean = false,
    val hasGlaucoma: Boolean = false,
    val description: String = ""
)