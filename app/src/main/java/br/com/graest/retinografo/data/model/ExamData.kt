package br.com.graest.retinografo.data.model

import android.location.Location
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.nio.ByteBuffer
import java.util.Date
import java.util.UUID

@Entity(
    tableName = "exam_data",
    foreignKeys = [ForeignKey(
        entity = PatientData::class,
        parentColumns = ["patientId"],
        childColumns = ["patientId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["patientId"])]
)
data class ExamData (
    val imagePath1: String,
    val imagePath2: String,
    val imagePath3: String,
    val imagePath4: String,
    val examTime: Long = System.currentTimeMillis(),
    val examCoordinates: String,
    val examLocation: String = "Default Input",
    val patientId: ByteArray,
    @PrimaryKey
    val id: ByteArray = ByteBuffer.wrap(ByteArray(16)).putLong(UUID.randomUUID().mostSignificantBits).putLong(UUID.randomUUID().leastSignificantBits).array()
)
//adicionar um campo para identificar se o exame está sincronizado com a parte do servidor