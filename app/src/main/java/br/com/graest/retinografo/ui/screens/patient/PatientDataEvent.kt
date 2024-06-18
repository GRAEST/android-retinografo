package br.com.graest.retinografo.ui.screens.patient

import br.com.graest.retinografo.data.model.DialogType
import br.com.graest.retinografo.data.model.PatientData

sealed interface PatientDataEvent {
    object SavePatientData : PatientDataEvent

    data class SetPatientName(val name: String) : PatientDataEvent
    data class SetPatientAge(val age: String) : PatientDataEvent
    data class ShowPatientDialog (val id: Int, val dialogType: DialogType) : PatientDataEvent
    object HideDialog : PatientDataEvent
    data class DeletePatientData(val id: Int) : PatientDataEvent
}