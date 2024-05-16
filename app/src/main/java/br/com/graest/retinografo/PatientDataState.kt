package br.com.graest.retinografo

import br.com.graest.retinografo.data.PatientData
import br.com.graest.retinografo.data.SortPatientType

data class PatientDataState (
    val patientData: List<PatientData> = emptyList(),
    val name: String = "",
    val age: String = "",
    val isAddingPatientData: Boolean = false,
    val sortPatientDataType : SortPatientType = SortPatientType.PATIENT_NAME
)