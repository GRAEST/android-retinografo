package br.com.graest.retinografo.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.graest.retinografo.utils.Converters
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "patient_data")
data class PatientData(
    @PrimaryKey
    val patientId: ByteArray = ByteArray(0),
    @TypeConverters(Converters::class) val dataCreated: LocalDateTime,
    val profilePicturePath: String,
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
) {
    @RequiresApi(Build.VERSION_CODES.O)
    constructor(
        patientId: ByteArray = ByteArray(0),
        profilePicturePath: String = "",
        name: String,
        birthDate: LocalDate? = null,
        gender: Gender = Gender.OTHER,
        cpf: String = "",
        email: String = "",
        telNumber: String = "",
        isDiabetic: Boolean = false,
        hasHyperTension: Boolean = false,
        hasGlaucoma: Boolean = false,
        description: String = ""
    ) : this(
        patientId,
        LocalDateTime.now(),
        profilePicturePath,
        name,
        birthDate,
        gender,
        cpf,
        email,
        telNumber,
        isDiabetic,
        hasHyperTension,
        hasGlaucoma,
        description
    )
}


