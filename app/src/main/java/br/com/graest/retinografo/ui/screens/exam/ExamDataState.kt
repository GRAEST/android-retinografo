package br.com.graest.retinografo.ui.screens.exam

import androidx.room.ColumnInfo
import br.com.graest.retinografo.data.model.ExamData
import br.com.graest.retinografo.data.model.PatientData

data class ExamDataState(
    val examsData: List<ExamData?> = emptyList(),
    val id: Int = 0,
    @ColumnInfo(name = "image_data") val image: ByteArray = ByteArray(1)
)