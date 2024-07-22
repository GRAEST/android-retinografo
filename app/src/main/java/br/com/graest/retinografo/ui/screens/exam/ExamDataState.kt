package br.com.graest.retinografo.ui.screens.exam

import br.com.graest.retinografo.data.model.ExamDataWithPatient
import br.com.graest.retinografo.data.model.PatientData

data class ExamDataState(
    val examsDataWithPatient: List<ExamDataWithPatient?> = emptyList(),
    var rightEyeImagePaths: List<String> = emptyList(),
    val leftEyeImagePaths: List<String> = emptyList(),
    val examLocation: String = "",
    val showAddPatientDialog: Boolean = false,
    val isLocationAdded: Boolean = false,
    val showToastRed: Boolean = false,
    val redToastMessage: String = "",
    val showToastGreen: Boolean = false,
    val greenToastMessage: String = "",
    val patientSelected: Boolean = false,
    val readyToSave: Boolean = false,
    val onLeftEyeSaveMode: Boolean = true,
    val onRightEyeSaveMode: Boolean = false,
    val patientData: PatientData? = null,
)