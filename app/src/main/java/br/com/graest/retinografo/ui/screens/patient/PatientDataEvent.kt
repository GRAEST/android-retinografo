package br.com.graest.retinografo.ui.screens.patient

import android.content.Context
import br.com.graest.retinografo.data.model.Gender

sealed interface PatientDataEvent {
    data class SavePatientData(val context: Context) : PatientDataEvent
    data object ClickEditImage : PatientDataEvent
    data class ShowEditPatientDialog(val id: ByteArray) : PatientDataEvent
    data object ShowAddPatientDialog : PatientDataEvent
    data object HideDialog : PatientDataEvent
    data class DeletePatientDataById(val id: ByteArray) : PatientDataEvent
    data class SetPatientName(val name: String) : PatientDataEvent
    data class SetPatientBirthDate(val birthDate: String) : PatientDataEvent
    data class SetPatientGender(val gender: Gender) : PatientDataEvent
    data class SetPatientCPF(val cpf: String) : PatientDataEvent
    data class SetPatientEmail(val email: String) : PatientDataEvent
    data class SetPatientTelNumber(val telNumber: String): PatientDataEvent
    data class SetIsDiabetic(val isDiabetic: Boolean): PatientDataEvent
    data class SetHasHyperTension(val hasHyperTension: Boolean): PatientDataEvent
    data class SetHasGlaucoma(val hasGlaucoma: Boolean): PatientDataEvent
    data class SetDescription(val description: String): PatientDataEvent
    data object ExpandGenderMenu: PatientDataEvent
    data object ShrinkGenderMenu: PatientDataEvent
}