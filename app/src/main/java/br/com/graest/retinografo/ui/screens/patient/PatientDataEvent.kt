package br.com.graest.retinografo.ui.screens.patient

import br.com.graest.retinografo.data.model.PatientData

sealed interface PatientDataEvent {
    object SavePatientData : PatientDataEvent

    data class SetPatientName(val name: String) : PatientDataEvent
    data class SetPatientAge(val age: String) : PatientDataEvent
    data class ShowEditPatientDialog (val id: Int) : PatientDataEvent
    object ShowAddPatientDialog : PatientDataEvent
    object HideDialog : PatientDataEvent
    data class DeletePatientDataById(val id: Int) : PatientDataEvent
    object DeletePatientData : PatientDataEvent
}