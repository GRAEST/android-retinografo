package br.com.graest.retinografo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.nio.ByteBuffer
import java.util.UUID

@Entity (
    tableName = "patient_data"
)
data class PatientData(
    val name: String,
    val age: Int = 0,
    val dataCreated: Long = System.currentTimeMillis(),
    val image: ByteArray = ByteArray(1),
    @PrimaryKey
    val patientId: ByteArray = ByteArray(1)
)
