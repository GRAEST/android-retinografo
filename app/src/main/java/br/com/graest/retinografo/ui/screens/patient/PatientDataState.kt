package br.com.graest.retinografo.ui.screens.patient

import br.com.graest.retinografo.data.model.PatientData
import br.com.graest.retinografo.data.model.SortPatientType

data class PatientDataState (
    val patientData: List<PatientData> = emptyList(),
    val name: String = "",
    val age: String = "",
    val isAddingPatientData: Boolean = false,
    val sortPatientDataType : SortPatientType = SortPatientType.PATIENT_NAME
)