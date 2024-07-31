package br.com.graest.retinografo.ui.screens.exam

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.camera.view.LifecycleCameraController
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.graest.retinografo.data.local.ExamDataDao
import br.com.graest.retinografo.data.model.ExamData
import br.com.graest.retinografo.utils.FlashLightController
import br.com.graest.retinografo.utils.LocationService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File


class ExamDataViewModel(
    private val examDataDao: ExamDataDao
) : ViewModel() {

    
    private val _examsDataWithPatient = examDataDao.getExamDataWithPatients()

    private val _examDataState = MutableStateFlow(ExamDataState())

    val examDataState =
        combine(_examDataState, _examsDataWithPatient) { examDataState, examsDataWithPatient ->
            examDataState.copy(
                examsDataWithPatient = examsDataWithPatient
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExamDataState())


    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: ExamDataEvent) {
        when (event) {
            is ExamDataEvent.DeleteExamDataById -> {
                viewModelScope.launch {
                    examDataDao.deleteExamById(event.id)
                }
            }

            is ExamDataEvent.SaveExamData -> {
                saveExamWithLocation(event.context)
            }

            ExamDataEvent.ShowAddPatientDialog -> {
                _examDataState.update {
                    it.copy(
                        showAddPatientDialog = true
                    )
                }
            }

            ExamDataEvent.HideAddPatientDialog -> {
                _examDataState.update {
                    it.copy(
                        showAddPatientDialog = false
                    )
                }
            }

            ExamDataEvent.SetIsLocationAddedFalse -> {
                _examDataState.update {
                    it.copy(
                        isLocationAdded = false,
                    )
                }
            }

            ExamDataEvent.SetIsLocationAddedTrue -> {
                _examDataState.update {
                    it.copy(
                        isLocationAdded = true
                    )
                }
            }

            is ExamDataEvent.SetExamLocation -> {
                _examDataState.update {
                    it.copy(
                        examLocation = event.examLocation
                    )
                }
            }

            is ExamDataEvent.PatientSelected -> {
                _examDataState.update {
                    it.copy(
                        patientData = event.patientData,
                        patientSelected = true
                    )
                }
            }
            ExamDataEvent.NoPatientSelected -> {
                _examDataState.update {
                    it.copy(
                        patientData = null,
                        patientSelected = false,
                        isLocationAdded = false
                    )
                }
            }

            ExamDataEvent.OnRightEyeSaveMode -> {
                _examDataState.update {
                    it.copy(
                        onRightEyeSaveMode = true,
                        onLeftEyeSaveMode = false
                    )
                }
            }
            ExamDataEvent.OnLeftEyeSaveMode -> {
                _examDataState.update {
                    it.copy(
                        onRightEyeSaveMode = false,
                        onLeftEyeSaveMode = true
                    )
                }
            }

            ExamDataEvent.OnReadyToSave -> {
                _examDataState.update {
                    it.copy(
                        readyToSave = true
                    )
                }
            }

            is ExamDataEvent.OnShowToastRed -> {
                viewModelScope.launch {
                    _examDataState.update {
                        it.copy(
                            redToastMessage = event.message,
                            showToastRed = true
                        )
                    }
                    delay(2000)
                    _examDataState.update {
                        it.copy(showToastRed = false)
                    }
                }
            }

            is ExamDataEvent.OnShowToastGreen -> {
                viewModelScope.launch {
                    _examDataState.update {
                        it.copy(
                            greenToastMessage = event.message,
                            showToastGreen = true
                        )
                    }
                    delay(2000)
                    _examDataState.update {
                        it.copy(showToastGreen = false)
                    }
                }
            }

            ExamDataEvent.OnCancelExam -> {
                cleanupPath()
                //cleanupTemporaryImages()
                _examDataState.update {
                    it.copy(
                        examLocation = "",
                        isLocationAdded = false,
                        readyToSave = false,
                        onLeftEyeSaveMode = true,
                        onRightEyeSaveMode = false
                    )
                }
            }
            is ExamDataEvent.OnShowExamDetails -> {
                viewModelScope.launch {
                    examDataDao.getExamById(event.id).collect { data ->
                        _examDataState.update {
                            it.copy(
                                examData = data,
                                patientData = event.patientData
                            )
                        }
                    }
                }
            }
            is ExamDataEvent.OnShowImageDetails -> {
                viewModelScope.launch {
                    _examDataState.update {
                        it.copy(
                            imageDetailIndex = event.index,
                            onShowImageDetail = true,
                            imageDetailSide = event.side
                        )
                    }
                }
            }

            ExamDataEvent.OnHideImageDetails -> {
                viewModelScope.launch {
                    _examDataState.update {
                        it.copy(
                            onShowImageDetail = false
                        )
                    }
                }
            }

            is ExamDataEvent.SetZoom -> {
                viewModelScope.launch {
                    _examDataState.update {
                        it.copy(
                            zoomRatio = event.newValue
                        )
                    }
                    _examDataState.value.cameraControl?.setZoomRatio(event.newValue) ?: run {
                    }
                }
            }

            is ExamDataEvent.SetFlash -> {
                viewModelScope.launch {
                    _examDataState.update {
                        it.copy(
                            setFlash = event.setFlash
                        )
                    }
                }
            }

            else -> {}
        }
    }

    fun setCameraController(controller: LifecycleCameraController) {
        Log.d("CameraZoom", "Setting camera controller.")
        val cameraControl = controller.cameraControl
        val cameraInfo = controller.cameraInfo

        if (cameraControl == null || cameraInfo == null) {
            Log.e("CameraZoom", "CameraControl or CameraInfo is null.")
            return
        }

        _examDataState.update {
            it.copy(
                cameraControl = cameraControl,
                cameraInfo = cameraInfo
            )
        }
    }

    fun addRightEyeImagePath(path: String) {
        _examDataState.update {
            it.copy(
                rightEyeImagePaths = _examDataState.value.rightEyeImagePaths + path
            )
        }
    }

    fun addLeftEyeImagePath(path: String) {
        _examDataState.update {
            it.copy(
                leftEyeImagePaths = _examDataState.value.leftEyeImagePaths + path
            )
        }
    }

    private fun cleanupPath() {
        _examDataState.update {
            it.copy(
                rightEyeImagePaths = emptyList(),
                leftEyeImagePaths = emptyList()
            )
        }
    }

    fun setErrorMessage(message: String?) {
        _examDataState.update {
            it.copy(
                errorMessage = message
            )
        }
    }

    //going to be used later
    private fun cleanupImages() {
        _examDataState.value.leftEyeImagePaths.forEach { path ->
            File(path).delete()
        }
        _examDataState.value.rightEyeImagePaths.forEach { path ->
            File(path).delete()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveExamWithLocation(context: Context) {
        val locationService = LocationService(context)
        locationService.getCurrentLocation { location ->
            Log.d("ExamDataViewModel", "Location obtained: $location")
            val examData = createExamData(context, location?.latitude, location?.longitude)
            viewModelScope.launch {
                try {
                    if (examData != null) {
                        examDataDao.insertExam(examData = examData)
                    }
                } catch (e: Exception) {
                    Log.e("ExamDataViewModel", "Error inserting ExamData", e)
                    setErrorMessage("Error saving exam data")
                } finally {
                    onEvent(ExamDataEvent.OnCancelExam)
                    onEvent(ExamDataEvent.SetIsLocationAddedFalse)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createExamData(context: Context, latitude: Double?, longitude: Double?): ExamData? {
        return try {

            val patientId = _examDataState.value.patientData?.patientId

            patientId?.let {
                ExamData(
                    listImagesRightEye = examDataState.value.rightEyeImagePaths,
                    listImagesLeftEye = examDataState.value.leftEyeImagePaths,
                    examCoordinates = "$latitude,$longitude",
                    examLocation = examDataState.value.examLocation,
                    patientId = it
                )
            }
        } catch (e: Exception) {
            Log.e("ExamDataViewModel", "Error creating ExamData", e)
            null
        }
    }

}