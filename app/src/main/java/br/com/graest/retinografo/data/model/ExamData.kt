package br.com.graest.retinografo.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.graest.retinografo.utils.Converters
import br.com.graest.retinografo.utils.LocalDateSerializer
import br.com.graest.retinografo.utils.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.nio.ByteBuffer
import java.time.LocalDateTime
import java.util.UUID

@Serializable
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
    @TypeConverters(Converters::class) val listImagesLeftEye: List<String>,
    @TypeConverters(Converters::class) val listImagesRightEye: List<String>,
    @TypeConverters(Converters::class) val listBinaryLeftEye: List<ByteArray>,
    @TypeConverters(Converters::class) val listBinaryRightEye: List<ByteArray>,
    @Serializable(with = LocalDateTimeSerializer::class)
    @TypeConverters(Converters::class) val examTime: LocalDateTime,
    val examCoordinates: String,
    val examLocation: String,
    val sentToServer: Boolean,
    val closed: Boolean,
    val patientId: ByteArray,
    @PrimaryKey val id: ByteArray
) {

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(
        listImagesLeftEye: List<String> = listOf(""),
        listImagesRightEye: List<String> = listOf(""),
        listBinaryLeftEye: List<ByteArray> = listOf(),
        listBinaryRightEye: List<ByteArray> = listOf(),
        examCoordinates: String,
        examLocation: String = "Default Input",
        sentToServer: Boolean = false,
        closed: Boolean = false,
        patientId: ByteArray
    ) : this(
        listImagesLeftEye,
        listImagesRightEye,
        listBinaryLeftEye,
        listBinaryRightEye,
        LocalDateTime.now(),
        examCoordinates,
        examLocation,
        sentToServer,
        closed,
        patientId,
        ByteBuffer.wrap(ByteArray(16)).putLong(UUID.randomUUID().mostSignificantBits).putLong(UUID.randomUUID().leastSignificantBits).array()
    )
}
