package br.com.graest.retinografo.ui.screens.exam

import androidx.room.ColumnInfo
import br.com.graest.retinografo.data.model.ExamData
import br.com.graest.retinografo.data.model.PatientData

data class ExamDataState(
    val examsData: List<ExamData?> = emptyList(),
    val id: Int = 0, //Exam ID
    val image1: ByteArray = ByteArray(1),
    val image2: ByteArray = ByteArray(1),
    val image3: ByteArray = ByteArray(1),
    val image4: ByteArray = ByteArray(1),
    val showDialog: Boolean = false,
    val showToast: Boolean = false,
    val patientSelected: Boolean = false,
    val patientData: PatientData? = null,
)