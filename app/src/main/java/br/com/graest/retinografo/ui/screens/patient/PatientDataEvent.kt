package br.com.graest.retinografo.ui.screens.patient

import android.content.Context
import br.com.graest.retinografo.data.model.PatientData

sealed interface PatientDataEvent {
    data class SavePatientData(val context: Context) : PatientDataEvent
    data class SetPatientName(val name: String) : PatientDataEvent
    data class SetPatientAge(val age: String) : PatientDataEvent
    data object ClickEditImage : PatientDataEvent
    data class ShowEditPatientDialog (val id: ByteArray) : PatientDataEvent
    data object ShowAddPatientDialog : PatientDataEvent
    data object HideDialog : PatientDataEvent
    data class DeletePatientDataById(val id: ByteArray) : PatientDataEvent
}