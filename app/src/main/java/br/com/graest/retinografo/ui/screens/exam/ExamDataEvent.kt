package br.com.graest.retinografo.ui.screens.exam

import android.content.Context
import br.com.graest.retinografo.data.model.PatientData

sealed interface ExamDataEvent {
    data class SaveExamData(val context: Context) : ExamDataEvent
    data class DeleteExamDataById(val id: ByteArray) : ExamDataEvent
    data object ShowAddPatientDialog: ExamDataEvent
    data object HideAddPatientDialog: ExamDataEvent
    data object SetIsLocationAddedFalse: ExamDataEvent
    data object SetIsLocationAddedTrue: ExamDataEvent
    data class SetExamLocation(val examLocation: String): ExamDataEvent
    data class PatientSelected(val patientData: PatientData): ExamDataEvent
    data object NoPatientSelected: ExamDataEvent
    data object OnRightEyeSaveMode: ExamDataEvent
    data object OnLeftEyeSaveMode: ExamDataEvent
    data object OnReadyToSave: ExamDataEvent
    data class OnShowToastRed(val message: String): ExamDataEvent
    data class OnShowToastGreen(val message: String): ExamDataEvent
    data object OnCancelExam: ExamDataEvent
    data class OnShowExamDetails(val id: ByteArray, val patientData: PatientData, val goToDetails:  () -> Unit) : ExamDataEvent
}