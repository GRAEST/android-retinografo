package br.com.graest.retinografo.ui.screens.patient

import br.com.graest.retinografo.data.model.PatientData

sealed interface PatientDataEvent {
    data object SavePatientData : PatientDataEvent
    data class SetPatientName(val name: String) : PatientDataEvent
    data class SetPatientAge(val age: String) : PatientDataEvent
    data object ClickEditImage : PatientDataEvent
    data class ShowEditPatientDialog (val id: Int) : PatientDataEvent
    data object ShowAddPatientDialog : PatientDataEvent
    data object HideDialog : PatientDataEvent
    data class DeletePatientDataById(val id: Int) : PatientDataEvent
}