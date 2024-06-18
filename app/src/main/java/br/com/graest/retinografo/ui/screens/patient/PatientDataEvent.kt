package br.com.graest.retinografo.ui.screens.patient

sealed interface PatientDataEvent {
    object SavePatientData : PatientDataEvent

    data class SetPatientName(val name: String) : PatientDataEvent
    data class SetPatientAge(val age: String) : PatientDataEvent
    data class ShowEditPatientDialog (val id: Int) : PatientDataEvent
    object ShowAddPatientDialog : PatientDataEvent
    object HideDialog : PatientDataEvent
    data class DeletePatientData(val id: Int) : PatientDataEvent
}