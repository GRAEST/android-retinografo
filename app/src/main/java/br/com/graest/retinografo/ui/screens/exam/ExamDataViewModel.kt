package br.com.graest.retinografo.ui.screens.exam

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.graest.retinografo.data.local.ExamDataDao
import br.com.graest.retinografo.data.model.ExamData
import br.com.graest.retinografo.utils.ExamCameraUtils.saveImageToFile
import br.com.graest.retinografo.utils.LocationService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File


class ExamDataViewModel(
    private val examDataDao: ExamDataDao,
) : ViewModel() {

    //error de capturar mensagem
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _examsDataWithPatient = examDataDao.getExamDataWithPatients()

    private val _examDataState = MutableStateFlow(ExamDataState())

    val examDataState =
        combine(_examDataState, _examsDataWithPatient) { examDataState, examsDataWithPatient ->
            examDataState.copy(
                examsDataWithPatient = examsDataWithPatient
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExamDataState())


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

            ExamDataEvent.OnShowToastRed -> {
                viewModelScope.launch {
                    _examDataState.update {
                        it.copy(showToastRed = true)
                    }
                    delay(2000)
                    _examDataState.update {
                        it.copy(showToastRed = false)
                    }
                }
            }

            ExamDataEvent.OnShowToastGreen -> {
                viewModelScope.launch {
                    _examDataState.update {
                        it.copy(showToastGreen = true)
                    }
                    delay(2000)
                    _examDataState.update {
                        it.copy(showToastGreen = false)
                    }
                }
            }

            ExamDataEvent.OnCancelExam -> {
                cleanupPath()
                cleanupTemporaryImages()
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

            else -> {}
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
        _errorMessage.value = message
    }


    private fun cleanupTemporaryImages() {
        _examDataState.value.leftEyeImagePaths.forEach { path ->
            File(path).delete()
        }
        _examDataState.value.rightEyeImagePaths.forEach { path ->
            File(path).delete()
        }
    }


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

    private fun createExamData(context: Context, latitude: Double?, longitude: Double?): ExamData? {
        return try {
            val leftEyeImagePathList = mutableListOf<String>()
            val rightEyeImagePathList = mutableListOf<String>()

            //talvez levar o file direto de temp para permanente ao invés de traduzir no meio
            _examDataState.value.leftEyeImagePaths.forEachIndexed { index, tempImagePath ->
                val bitmapImageFromTemp = BitmapFactory.decodeFile(tempImagePath)
                leftEyeImagePathList.add(saveImageToFile(context, bitmapImageFromTemp, "left_eye_image${index}_${System.currentTimeMillis()}.jpg") ?: "")
            }

            _examDataState.value.rightEyeImagePaths.forEachIndexed { index, tempImagePath ->
                val bitmapImageFromTemp = BitmapFactory.decodeFile(tempImagePath)
                rightEyeImagePathList.add(saveImageToFile(context, bitmapImageFromTemp, "right_eye_image${index}_${System.currentTimeMillis()}.jpg") ?: "")
            }

            val patientId = _examDataState.value.patientData?.patientId

            patientId?.let {
                ExamData(
                    listImagesRightEye = rightEyeImagePathList,
                    listImagesLeftEye = leftEyeImagePathList,
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