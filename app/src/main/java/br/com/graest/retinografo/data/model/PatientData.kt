package br.com.graest.retinografo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.nio.ByteBuffer
import java.util.UUID

@Entity (
    tableName = "patient_data"
)
data class PatientData(
    @PrimaryKey
    val patientId: ByteArray = ByteArray(0),
    val dataCreated: Long = System.currentTimeMillis(),
    val profilePicture: ByteArray = ByteArray(0),
    val name: String,
    val age: Int = 0,
    val gender: Gender = Gender.OTHER,
    val cpf: String = "",
    val email: String = "",
    val telNumber: String = "",
    val isDiabetic: Boolean = false,
    val hasHyperTension: Boolean = false,
    val hasGlaucoma: Boolean = false,
    val description: String = ""
)

// Enum for Gender
enum class Gender {
    MALE, FEMALE, OTHER
}

// Converters for Room to handle the Gender enum
class Converters {
    @TypeConverter
    fun fromGender(value: Gender): String {
        return value.name
    }

    @TypeConverter
    fun toGender(value: String): Gender {
        return Gender.valueOf(value)
    }
}