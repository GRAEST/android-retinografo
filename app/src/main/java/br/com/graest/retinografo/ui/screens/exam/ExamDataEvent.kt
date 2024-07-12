package br.com.graest.retinografo.ui.screens.exam

import android.graphics.Bitmap
import br.com.graest.retinografo.data.model.PatientData
import br.com.graest.retinografo.ui.screens.patient.PatientDataEvent

sealed interface ExamDataEvent {
    object SaveExamData : ExamDataEvent
    data class SetExamImage(val image: ByteArray) : ExamDataEvent
    data class DeleteExamDataById(val id: Int) : ExamDataEvent
    object DeleteExamData : ExamDataEvent
    object ShowDialog: ExamDataEvent
    object HideDialog: ExamDataEvent
    data class PatientSelected(val patientData: PatientData): ExamDataEvent
    object NoPatientSelected: ExamDataEvent
    object OnShowToast: ExamDataEvent
}