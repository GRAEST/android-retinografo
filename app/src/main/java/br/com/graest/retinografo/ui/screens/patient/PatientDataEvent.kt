package br.com.graest.retinografo.ui.screens.patient

import br.com.graest.retinografo.data.model.PatientData
import br.com.graest.retinografo.data.model.SortPatientType

sealed interface PatientDataEvent {
    object SavePatientData : PatientDataEvent
    data class SetPatientName(val name: String) : PatientDataEvent
    data class SetPatientAge(val age: String) : PatientDataEvent
    object ShowDialog : PatientDataEvent
    object HideDialog : PatientDataEvent
    data class SortPatientData(val sortPatientType: SortPatientType) : PatientDataEvent
    data class DeletePatientData(val patientData: PatientData) : PatientDataEvent
}