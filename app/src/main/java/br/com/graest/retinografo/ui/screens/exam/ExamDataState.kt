package br.com.graest.retinografo.ui.screens.exam

import br.com.graest.retinografo.data.model.ExamDataWithPatient
import br.com.graest.retinografo.data.model.PatientData

data class ExamDataState(
    val examsDataWithPatient: List<ExamDataWithPatient?> = emptyList(),
    val id: Int = 0, //Exam ID //acho que não é usado
    val examLocation: String = "",
    val showAddPatientDialog: Boolean = false,
    val isLocationAdded: Boolean = false,
    val showToastRed: Boolean = false,
    val showToastGreen: Boolean = false,
    val patientSelected: Boolean = false,
    val patientData: PatientData? = null,
)