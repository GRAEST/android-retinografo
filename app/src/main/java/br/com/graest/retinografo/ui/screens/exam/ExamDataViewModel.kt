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
    private val examDataDao: ExamDataDao
) : ViewModel() {

    private var _capturedImagePaths = MutableStateFlow<List<String>>(emptyList())
    val capturedImagePaths: StateFlow<List<String>> = _capturedImagePaths.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _examsDataWithPatient = examDataDao.getExamDataWithPatients()

    private val _examDataState = MutableStateFlow(ExamDataState())

    val examDataState = combine(_examDataState, _examsDataWithPatient) { examDataState, examsDataWithPatient ->
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
            is ExamDataEvent.SaveExamData -> { saveExamWithLocation(event.context) }

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
                        isLocationAdded = false
                    )
                }
            }
            else -> {}
        }
    }

    fun addImagePath(path: String) {
        if (_capturedImagePaths.value.size < 4) {
            _capturedImagePaths.value += path
        }
    }

    private fun cleanupPath() {
        _capturedImagePaths.value = emptyList()
    }

    fun setErrorMessage(message: String?) {
        _errorMessage.value = message
    }

    private fun cleanupTemporaryImages() {
        capturedImagePaths.value.forEach { path ->
            File(path).delete()
        }
    }

    private fun saveExamWithLocation(context: Context) {
        val locationService = LocationService(context)
        locationService.getCurrentLocation { location ->
            Log.d("ExamDataViewModel", "Location obtained: $location")
            if (_capturedImagePaths.value.size == 4) {
                val examData = createExamData(context, location?.latitude, location?.longitude)
                if (examData != null) {
                    viewModelScope.launch {
                        try {
                            examDataDao.insertExam(examData)
                            Log.d("ExamDataViewModel", "ExamData inserted successfully")
                        } catch (e: Exception) {
                            Log.e("ExamDataViewModel", "Error inserting ExamData", e)
                            setErrorMessage("Error saving exam data")
                        } finally {
                            onEvent(ExamDataEvent.OnCancelExam)
                            onEvent(ExamDataEvent.SetIsLocationAddedFalse)
                        }
                    }
                }
            } else {
                setErrorMessage("Not enough images captured")
                Log.e("ExamDataViewModel", "Not enough images captured")
            }
        }
    }

    private fun createExamData(context: Context, latitude: Double?, longitude: Double?): ExamData? {
        return try {
            val image1 = BitmapFactory.decodeFile(_capturedImagePaths.value[0])
            val image2 = BitmapFactory.decodeFile(_capturedImagePaths.value[1])
            val image3 = BitmapFactory.decodeFile(_capturedImagePaths.value[2])
            val image4 = BitmapFactory.decodeFile(_capturedImagePaths.value[3])
            val patientId = _examDataState.value.patientData?.patientId

            patientId?.let {
                ExamData(
                    imagePath1 = saveImageToFile(context, image1, "image1_${System.currentTimeMillis()}.jpg") ?: "",
                    imagePath2 = saveImageToFile(context, image2, "image2_${System.currentTimeMillis()}.jpg") ?: "",
                    imagePath3 = saveImageToFile(context, image3, "image3_${System.currentTimeMillis()}.jpg") ?: "",
                    imagePath4 = saveImageToFile(context, image4, "image4_${System.currentTimeMillis()}.jpg") ?: "",
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