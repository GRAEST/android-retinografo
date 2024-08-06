package br.com.graest.retinografo.ui.screens.exam

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.camera.view.LifecycleCameraController
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.graest.retinografo.data.local.ExamDataDao
import br.com.graest.retinografo.data.model.ExamData
import br.com.graest.retinografo.utils.CameraUtils.saveBitmapToExternalStorage
import br.com.graest.retinografo.utils.CameraUtils.takePhoto
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
                viewModelScope.launch {
                    saveExamWithLocation(event.context)
                    if (examDataState.value.errorMessage == ""){
                        //testar isso depois
                        //saveExamWithLocation(event.context)
                    }
                }
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
                viewModelScope.launch {
                    _examDataState.update {
                        it.copy(
                            examLocation = "",
                            isLocationAdded = false,
                            readyToSave = false,
                            onLeftEyeSaveMode = true,
                            onRightEyeSaveMode = false,
                            rightEyeBitmaps = emptyList(),
                            leftEyeBitmaps = emptyList()
                        )
                    }
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

            ExamDataEvent.OnSetNavigationStatusToFalse -> {
                viewModelScope.launch {
                    _examDataState.update {
                        it.copy(
                            onNavigateToDetails = false
                        )
                    }
                }
            }

            is ExamDataEvent.OnTakePhoto -> {
                viewModelScope.launch {
                    takePhoto(event.applicationContext, event.cameraController, event.onPhotoTaken)
                }
            }


            else -> {}
        }
    }

    fun setErrorMessage(message: String?) {
        _examDataState.update {
            it.copy(
                errorMessage = message
            )
        }
    }


    private fun cleanupCache() {
        _examDataState.update {
            it.copy(

            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getLatestExamAndNavigate(){
        viewModelScope.launch {
            examDataDao.getLatestExam().collect { data ->
                _examDataState.update {
                    it.copy(
                        examData = data.examData,
                        patientData = data.patientData,
                        onNavigateToDetails = true
                    )
                }
            }
        }
    }

    fun setNavigationStatusToFalse() {

        _examDataState.update {
            it.copy(
                onNavigateToDetails = false
            )
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveExamWithLocation(context: Context) {
        val locationService = LocationService(context)
        locationService.getCurrentLocation { location ->
            Log.d("ExamDataViewModel", "Location obtained: $location")
            viewModelScope.launch {
                val examData = createExamData(context, location?.latitude, location?.longitude)
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
                    onEvent(ExamDataEvent.NoPatientSelected)
                    getLatestExamAndNavigate()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun createExamData(context: Context, latitude: Double?, longitude: Double?): ExamData? {
        return try {

            val patientId = _examDataState.value.patientData?.patientId

            val rightEyeImagePath = mutableListOf<String>()
            val leftEyeImagePath = mutableListOf<String>()

            examDataState.value.rightEyeBitmaps.forEach { bitmap ->
                saveBitmapToExternalStorage(context, bitmap, System.currentTimeMillis().toString())?.let {
                    rightEyeImagePath.add(
                        it
                    )
                }
            }
            examDataState.value.leftEyeBitmaps.forEach { bitmap ->
                saveBitmapToExternalStorage(context, bitmap, System.currentTimeMillis().toString())?.let {
                    leftEyeImagePath.add(
                        it
                    )
                }
            }
            patientId?.let {
                ExamData(
                    listImagesRightEye = rightEyeImagePath,
                    listImagesLeftEye = leftEyeImagePath,
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

    fun onTakeRightEyePhoto(bitmap: Bitmap) {
        _examDataState.update {
            it.copy(
                rightEyeBitmaps = it.rightEyeBitmaps + bitmap
            )
        }
    }

    fun onTakeLeftEyePhoto(bitmap: Bitmap) {
        _examDataState.update {
            it.copy(
                leftEyeBitmaps = it.leftEyeBitmaps + bitmap
            )
        }
    }


}