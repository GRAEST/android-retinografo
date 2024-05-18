package br.com.graest.retinografo.ui.screens.patient

import br.com.graest.retinografo.data.patient.PatientData
import br.com.graest.retinografo.data.patient.SortPatientType

data class PatientDataState (
    val patientData: List<PatientData> = emptyList(),
    val name: String = "",
    val age: String = "",
    val isAddingPatientData: Boolean = false,
    val sortPatientDataType : SortPatientType = SortPatientType.PATIENT_NAME
)