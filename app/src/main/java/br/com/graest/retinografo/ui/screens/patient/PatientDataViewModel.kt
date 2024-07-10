package br.com.graest.retinografo.ui.screens.patient

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.graest.retinografo.data.local.PatientDataDao
import br.com.graest.retinografo.data.model.PatientData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class PatientDataViewModel(
    private val patientDataDao: PatientDataDao
) : ViewModel() {

    private val _patientsData = patientDataDao.getPatientsData()

    private val _patientDataState = MutableStateFlow(PatientDataState())

    val patientDataState = combine(_patientDataState, _patientsData) { patientState, patientsData ->
        patientState.copy(
            patientsData = patientsData
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PatientDataState())

    private val _capturedImagePath = MutableStateFlow<String?>(null)
    val capturedImagePath: StateFlow<String?> = _capturedImagePath.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun onEvent(event: PatientDataEvent) {
        when (event) {
            // Estamos com 2 Deletes, um deles pode ir embora
            PatientDataEvent.DeletePatientData -> {

                val patientData = PatientData(
                    patientDataState.value.name,
                    patientDataState.value.age.toInt(),
                    patientDataState.value.image,
                    patientDataState.value.id
                )

                viewModelScope.launch {
                    patientDataDao.deletePatientData(patientData)
                }
            }

            is PatientDataEvent.DeletePatientDataById -> {
                viewModelScope.launch {
                    patientDataDao.deletePatientDataById(event.id)
                }
                onEvent(PatientDataEvent.HideDialog)
            }

            PatientDataEvent.HideDialog -> {
                viewModelScope.launch {
                    _patientDataState.update {
                        it.copy(
                            isAddingPatientData = false,
                            isEditingPatientData = false,
                            id = 0,
                            name = "",
                            age = ""
                        )
                    }
                }
            }

            PatientDataEvent.ShowAddPatientDialog -> {
                _patientDataState.update {
                    it.copy(
                        isAddingPatientData = true
                    )
                }
            }

            is PatientDataEvent.ShowEditPatientDialog -> {
                viewModelScope.launch {
                    patientDataDao.getPatientData(event.id).collect { data ->
                        if (data != null) {
                            _patientDataState.update { currentState ->
                                currentState.copy(
                                    isEditingPatientData = true,
                                    id = event.id,
                                    name = data.name,
                                    age = data.age.toString()
                                )
                            }
                        }
                    }
                }
            }

            PatientDataEvent.SavePatientData -> {
                //Aquele problema com Create voltar a aparecer Edit ainda existe
                val id = patientDataState.value.id //id é necessário para caso de EDIT
                val age = patientDataState.value.age
                val name = patientDataState.value.name

                if (age.isBlank() || name.isBlank()) {
                    return
                }
                val patientData = PatientData(
                    id = id,
                    age = age.toInt(),
                    name = name,
                    image = ByteArray(1)
                )
                viewModelScope.launch {
                    patientDataDao.upsertPatientData(patientData)
                    onEvent(PatientDataEvent.HideDialog)
                }

            }

            is PatientDataEvent.SetPatientAge -> {
                _patientDataState.update {
                    it.copy(
                        age = event.age
                    )
                }
            }

            is PatientDataEvent.SetPatientName -> {
                _patientDataState.update {
                    it.copy(
                        name = event.name
                    )
                }
            }

            else -> {}
        }
    }

    fun setCapturedImagePath(path: String?) {
        _capturedImagePath.value = path
    }

    fun setErrorMessage(message: String?) {
        _errorMessage.value = message
    }

    fun onTakePhoto(bitmap: Bitmap) {

//        viewModelScope.launch {
//            val image = ExamData(image = bitmapToByteArray(bitmap))
//            //examDataDao.insertExam(image)
//            patientDataDao.insertImage(image)
//            // Update the _bitmaps state flow
//            //_bitmaps.value = _bitmaps.value + bitmap
//        }
    }
}