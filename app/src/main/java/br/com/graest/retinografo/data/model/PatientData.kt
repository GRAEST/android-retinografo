package br.com.graest.retinografo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity (
    tableName = "patient_data"
)
data class PatientData(
    @PrimaryKey
    val patientId: ByteArray = ByteArray(0),
    val dataCreated: Long = System.currentTimeMillis(),
    val profilePicture: ByteArray = ByteArray(0),
    val name: String,
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

