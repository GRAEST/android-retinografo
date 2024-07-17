package br.com.graest.retinografo.data.model

import android.location.Location
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

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
    val patientId: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)