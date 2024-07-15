package br.com.graest.retinografo.ui.screens.exam

import android.content.Context
import android.location.Location
import androidx.compose.runtime.MutableState
import br.com.graest.retinografo.data.model.PatientData

sealed interface ExamDataEvent {
    data class SaveExamData(val context: Context) : ExamDataEvent
    data class DeleteExamDataById(val id: Int) : ExamDataEvent
    data object ShowDialog: ExamDataEvent
    data object HideDialog: ExamDataEvent
    data class PatientSelected(val patientData: PatientData): ExamDataEvent
    data object NoPatientSelected: ExamDataEvent
    data object OnShowToastRed: ExamDataEvent
    data object OnShowToastGreen: ExamDataEvent
    data object OnCancelExam: ExamDataEvent
}