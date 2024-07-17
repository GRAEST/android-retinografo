package br.com.graest.retinografo.ui.screens.patient

import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.graest.retinografo.data.local.PatientDataDao
import br.com.graest.retinografo.data.model.PatientData
import br.com.graest.retinografo.utils.ImageConvertingUtils.bitmapToByteArray
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
                            isEditingImage = false,
                            patientId = ByteArray(1),
                            name = "",
                            age = ""
                        )
                    }
                }
            }

            PatientDataEvent.ClickEditImage -> {
                _patientDataState.update {
                    it.copy(
                        isEditingImage = true
                    )
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
                                    patientId = event.id,
                                    name = data.name,
                                    image = data.image,
                                    age = data.age.toString()
                                )
                            }
                        }
                    }
                }
            }

            PatientDataEvent.SavePatientData -> {
                /*
                * quanto você edita algo, por algum motivo fica salvo
                * então algumas vezes você tenta abrir um "criar Paciente"
                * e depois de criado o paciente, volta a abrir a tela de Edit
                * */
                val patientId = patientDataState.value.patientId //id é necessário para caso de EDIT
                val age = patientDataState.value.age
                val name = patientDataState.value.name
                val bitmap = BitmapFactory.decodeFile(capturedImagePath.value)

                if (age.isBlank() || name.isBlank()) {
                    return
                }
                val patientData = PatientData(
                    patientId = patientId,
                    age = age.toInt(),
                    name = name,
                    image = bitmapToByteArray(bitmap)
                )
                viewModelScope.launch {
                    patientDataDao.upsertPatientData(patientData)
                    //por algum motivo esse hide dialog não funciona da forma que deveria, ainda mais aqui
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

}