package br.com.graest.retinografo.ui.screens.patient

import br.com.graest.retinografo.data.model.PatientData
import br.com.graest.retinografo.data.model.SortPatientType

data class PatientDataState (
    val patientsData: List<PatientData> = emptyList(),
    val id: Int = 0,
    val name: String = "",
    val age: String = "",
    val isAddingPatientData: Boolean = false,
    val isEditingPatientData: Boolean = false,
)