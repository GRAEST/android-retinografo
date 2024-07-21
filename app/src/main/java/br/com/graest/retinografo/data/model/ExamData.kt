package br.com.graest.retinografo.data.model

import android.location.Location
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.graest.retinografo.utils.Converters
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
    @TypeConverters(Converters::class) val listImagesLeftEye: List<String> = listOf(""),
    @TypeConverters(Converters::class) val listImagesRightEye: List<String> = listOf(""),
    val examTime: Long = System.currentTimeMillis(),
    val examCoordinates: String,
    val examLocation: String = "Default Input",
    val sentToServer: Boolean = false,
    val patientId: ByteArray,
    @PrimaryKey
    val id: ByteArray = ByteBuffer.wrap(ByteArray(16)).putLong(UUID.randomUUID().mostSignificantBits).putLong(UUID.randomUUID().leastSignificantBits).array()
)
