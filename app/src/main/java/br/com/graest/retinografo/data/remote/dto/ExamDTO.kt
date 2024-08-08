package br.com.graest.retinografo.data.remote.dto

import br.com.graest.retinografo.data.model.PatientData
import br.com.graest.retinografo.utils.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ExamDTO (
    val rightEyeImageList: List<ByteArray>,
    val leftEyeImageList: List<ByteArray>,
    val rightEyeBinaryList: List<ByteArray>,
    val leftEyeIBinaryList: List<ByteArray>,
    @Serializable(with = LocalDateTimeSerializer::class)
    val examTime: LocalDateTime,
    val examCoordinates: String,
    val examLocation: String,
    val sentToServer: Boolean,
    val closed: Boolean,
    val id: ByteArray,
    val patientData: PatientData
)
