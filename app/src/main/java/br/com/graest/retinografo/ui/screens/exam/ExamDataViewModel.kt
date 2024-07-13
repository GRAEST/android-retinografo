package br.com.graest.retinografo.ui.screens.exam

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import br.com.graest.retinografo.data.local.ExamDataDao
import br.com.graest.retinografo.data.model.ExamData
import br.com.graest.retinografo.ui.screens.patient.PatientDataEvent
import br.com.graest.retinografo.ui.screens.patient.PatientDataState
import br.com.graest.retinografo.utils.ImageConvertingUtils.bitmapToByteArray
import br.com.graest.retinografo.utils.ImageConvertingUtils.byteArrayToBitmap
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExamDataViewModel(
    private val examDataDao: ExamDataDao
) : ViewModel() {

//    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
//    val bitmaps = _bitmaps.asStateFlow()
//
//
//    init {
//        viewModelScope.launch {
//            examDataDao.getExamData()
//                .collect { entities ->
//                    val loadedBitmaps = entities.map { entity ->
//                        byteArrayToBitmap(entity.image)
//                    }
//                    _bitmaps.value = loadedBitmaps
//                }
//        }
//    }


    private val _capturedImagePaths = MutableStateFlow<List<String>>(emptyList())
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


    //tem que melhorar bastante essa parte aqui,
    //mas digo que isso vai funcionar melhor quando o exame completo for implementado

    fun onEvent(event: ExamDataEvent) {
        when (event) {
            is ExamDataEvent.DeleteExamDataById -> {
                viewModelScope.launch {
                    examDataDao.deleteExamById(event.id)
                }
            }

            ExamDataEvent.SaveExamData -> {
                val image1 = BitmapFactory.decodeFile(capturedImagePaths.value[0])
                val image2 = BitmapFactory.decodeFile(capturedImagePaths.value[1])
                val image3 = BitmapFactory.decodeFile(capturedImagePaths.value[2])
                val image4 = BitmapFactory.decodeFile(capturedImagePaths.value[3])
                val patientId = examDataState.value.patientData?.patientId

                val examData= patientId?.let {
                    ExamData(
                        image1 = bitmapToByteArray(image1),
                        image2 = bitmapToByteArray(image2),
                        image3 = bitmapToByteArray(image3),
                        image4 = bitmapToByteArray(image4),
                        patientId = it
                    )
                }
                if (examData != null) {
                    viewModelScope.launch {
                        examDataDao.insertExam(examData)
                    }
                }

            }

            is ExamDataEvent.SetExamImage -> {
                viewModelScope.launch {
                    //examDataDao.insertExam(event.image)
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
            ExamDataEvent.OnShowToast -> {
                viewModelScope.launch {
                    _examDataState.update {
                        it.copy(showToast = true)
                    }
                    delay(2000)
                    _examDataState.update {
                        it.copy(showToast = false)
                    }
                }
            }

        }
    }

//    fun onTakePhoto(bitmap: Bitmap) {
//        viewModelScope.launch {
//            val image = ExamData(image = bitmapToByteArray(bitmap))
//            examDataDao.insertExam(image)
//            // Update the _bitmaps state flow
//            _bitmaps.value = _bitmaps.value + bitmap
//        }
//    }

    fun addImagePath(path: String) {
        _capturedImagePaths.value = if (_capturedImagePaths.value.size < 4) {
            _capturedImagePaths.value + path
        } else {
            _capturedImagePaths.value
        }
        //vou precisar de uma
    }

    fun setErrorMessage(message: String?) {
        _errorMessage.value = message
    }


}