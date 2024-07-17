package br.com.graest.retinografo.ui.screens.patient

import br.com.graest.retinografo.data.model.PatientData

data class PatientDataState(
    val patientsData: List<PatientData?> = emptyList(),
    val patientId: ByteArray? = null,
    val name: String = "",
    val age: String = "",
    val image: ByteArray = ByteArray(1),
    val isAddingPatientData: Boolean = false,
    val isEditingPatientData: Boolean = false,
    val isEditingImage: Boolean = false
)