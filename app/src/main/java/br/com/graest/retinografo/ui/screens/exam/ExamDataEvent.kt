package br.com.graest.retinografo.ui.screens.exam

import android.content.Context
import android.graphics.Bitmap
import br.com.graest.retinografo.data.model.PatientData
import br.com.graest.retinografo.ui.screens.patient.PatientDataEvent

sealed interface ExamDataEvent {
    data class SaveExamData(val context: Context) : ExamDataEvent
    data class SetExamImage(val image: ByteArray) : ExamDataEvent
    data class DeleteExamDataById(val id: Int) : ExamDataEvent
    data object ShowDialog: ExamDataEvent
    data object HideDialog: ExamDataEvent
    data class PatientSelected(val patientData: PatientData): ExamDataEvent
    data object NoPatientSelected: ExamDataEvent
    data object OnShowToastRed: ExamDataEvent
    data object OnShowToastGreen: ExamDataEvent
    data object OnCancelExam: ExamDataEvent
}