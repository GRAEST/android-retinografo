package br.com.graest.retinografo.ui.screens.exam

import androidx.room.ColumnInfo
import br.com.graest.retinografo.data.model.ExamData
import br.com.graest.retinografo.data.model.ExamDataWithPatient
import br.com.graest.retinografo.data.model.PatientData

data class ExamDataState(
    val examsDataWithPatient: List<ExamDataWithPatient?> = emptyList(),
    val id: Int = 0, //Exam ID
    val showDialog: Boolean = false,
    val showToastRed: Boolean = false,
    val showToastGreen: Boolean = false,
    val patientSelected: Boolean = false,
    val patientData: PatientData? = null,
)