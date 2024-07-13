package br.com.graest.retinografo.ui.screens.exam

import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.graest.retinografo.data.local.ExamDataDao
import br.com.graest.retinografo.data.model.ExamData
import br.com.graest.retinografo.utils.ExamCameraUtils.saveImageToFile
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

    private val _examsData = examDataDao.getExamData()

    private val _examDataState = MutableStateFlow(ExamDataState())

    val examDataState = combine(_examDataState, _examsData) { examDataState, examsData ->
        examDataState.copy(
            examsData = examsData
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

                val image1 = BitmapFactory.decodeFile(capturedImagePaths.value[0])
                val image2 = BitmapFactory.decodeFile(capturedImagePaths.value[1])
                val image3 = BitmapFactory.decodeFile(capturedImagePaths.value[2])
                val image4 = BitmapFactory.decodeFile(capturedImagePaths.value[3])
                val patientId = examDataState.value.patientData?.patientId

                val examData = patientId?.let {
                    ExamData(
                        imagePath1 = saveImageToFile(event.context, image1, "image1_${System.currentTimeMillis()}.jpg") ?: "",
                        imagePath2 = saveImageToFile(event.context, image2, "image2_${System.currentTimeMillis()}.jpg") ?: "",
                        imagePath3 = saveImageToFile(event.context, image3, "image3_${System.currentTimeMillis()}.jpg") ?: "",
                        imagePath4 = saveImageToFile(event.context, image4, "image4_${System.currentTimeMillis()}.jpg") ?: "",
                        patientId = it
                    )
                }
                if (examData != null) {
                    viewModelScope.launch {
                        examDataDao.insertExam(examData)
                    }
                }

            }

            ExamDataEvent.ShowDialog -> {
                _examDataState.update {
                    it.copy(
                        showDialog = true
                    )
                }
            }
            ExamDataEvent.HideDialog -> {
                _examDataState.update {
                    it.copy(
                        showDialog = false
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
                        patientSelected = false
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
            }
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

}