package br.com.graest.retinografo.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

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
    val image1: ByteArray,
    val image2: ByteArray,
    val image3: ByteArray,
    val image4: ByteArray,
    val patientId: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)