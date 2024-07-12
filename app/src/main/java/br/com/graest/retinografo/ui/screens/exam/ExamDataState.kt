package br.com.graest.retinografo.ui.screens.exam

import androidx.room.ColumnInfo
import br.com.graest.retinografo.data.model.ExamData
import br.com.graest.retinografo.data.model.PatientData

data class ExamDataState(
    val examsData: List<ExamData?> = emptyList(),
    val id: Int = 0,
    val image: ByteArray = ByteArray(1),
    val showDialog: Boolean = false,
    val showToast: Boolean = false,
    val patientSelected: Boolean = false,
    val patientData: PatientData? = null,
)