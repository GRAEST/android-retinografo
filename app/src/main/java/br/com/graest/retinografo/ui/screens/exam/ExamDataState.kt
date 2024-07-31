package br.com.graest.retinografo.ui.screens.exam

import androidx.camera.core.CameraControl
import androidx.camera.core.CameraInfo
import br.com.graest.retinografo.data.model.ExamData
import br.com.graest.retinografo.data.model.ExamDataWithPatient
import br.com.graest.retinografo.data.model.PatientData

data class ExamDataState(
    val examsDataWithPatient: List<ExamDataWithPatient?> = emptyList(),
    var rightEyeImagePaths: List<String> = emptyList(),
    val leftEyeImagePaths: List<String> = emptyList(),
    var rightEyeBinaryList: List<ByteArray> = emptyList(),
    val leftEyeBinaryList: List<ByteArray> = emptyList(),
    val errorMessage: String? = null,
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
    val examData: ExamData? = null,
    val patientData: PatientData? = null,
    val imageDetailIndex: Int = 0,
    val imageDetailSide: String = "",
    val onShowImageDetail: Boolean = false,
    val cameraControl: CameraControl? = null,
    val cameraInfo: CameraInfo? = null,
    val zoomRatio: Float = 1f,
)