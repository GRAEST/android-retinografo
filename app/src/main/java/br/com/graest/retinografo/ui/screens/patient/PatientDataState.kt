package br.com.graest.retinografo.ui.screens.patient

import br.com.graest.retinografo.data.model.Gender
import br.com.graest.retinografo.data.model.PatientData

data class PatientDataState(
    val patientsData: List<PatientData?> = emptyList(),
    val patientId: ByteArray? = null,
    val name: String = "",
    val age: String = "",
    val gender: Gender = Gender.OTHER,
    val cpf: String = "",
    val email: String = "",
    val telNumber: String = "",
    val isDiabetic: Boolean = false,
    val hasHyperTension: Boolean = false,
    val hasGlaucoma: Boolean = false,
    val description: String = "",
    val profilePicture: ByteArray = ByteArray(0),
    val isAddingPatientData: Boolean = false,
    val isEditingPatientData: Boolean = false,
    val isEditingImage: Boolean = false,
    val genderMenuExpanded: Boolean = false
)