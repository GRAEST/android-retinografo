package br.com.graest.retinografo.ui.screens.patient

import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.graest.retinografo.R
import br.com.graest.retinografo.data.local.PatientDataDao
import br.com.graest.retinografo.data.model.Gender
import br.com.graest.retinografo.data.model.PatientData
import br.com.graest.retinografo.utils.ImageConvertingUtils.bitmapToByteArray
import br.com.graest.retinografo.utils.ImageConvertingUtils.getBitmapFromDrawable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.nio.ByteBuffer
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class PatientDataViewModel(
    private val patientDataDao: PatientDataDao,
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
                            patientId = ByteArray(0),
                            name = "",
                            age = "",
                            gender = Gender.OTHER,
                            cpf = "",
                            email = "",
                            telNumber = "",
                            isDiabetic = false,
                            hasHyperTension = false,
                            hasGlaucoma = false,
                            description = ""
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
                        isAddingPatientData = true,
                        patientId = null,
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
                                    profilePicture = data.profilePicture,
                                    age = data.age.toString(),
                                    gender = data.gender,
                                    cpf = data.cpf,
                                    email = data.email,
                                    telNumber = data.telNumber,
                                    isDiabetic = data.isDiabetic,
                                    hasHyperTension = data.hasHyperTension,
                                    hasGlaucoma = data.hasGlaucoma,
                                    description = data.description
                                )
                            }
                        }
                    }
                }
            }

            is PatientDataEvent.SavePatientData -> {
                /*
                * quanto você edita algo, por algum motivo fica salvo
                * então algumas vezes você tenta abrir um "criar Paciente"
                * e depois de criado o paciente, volta a abrir a tela de Edit
                * */


                val patientId = patientDataState.value.patientId //id é necessário para caso de EDIT
                val name = patientDataState.value.name
                val age = patientDataState.value.age
                val gender = patientDataState.value.gender
                val cpf = patientDataState.value.cpf
                val email = patientDataState.value.email
                val telNumber = patientDataState.value.telNumber
                val isDiabetic = patientDataState.value.isDiabetic
                val hasHyperTension = patientDataState.value.hasHyperTension
                val hasGlaucoma = patientDataState.value.hasGlaucoma
                val description = patientDataState.value.description


                val bitmap =  if (capturedImagePath.value == null){
                    getBitmapFromDrawable(event.context, R.drawable.user_icon)
                } else {
                    BitmapFactory.decodeFile(capturedImagePath.value)
                }


                val newPatientId = patientId ?: ByteBuffer.wrap(ByteArray(16))
                    .putLong(UUID.randomUUID().mostSignificantBits)
                    .putLong(UUID.randomUUID().leastSignificantBits)
                    .array()


                if (age.isBlank() || name.isBlank()) {
                    return
                }
                val patientData = if (patientId != null) {
                    PatientData(
                        patientId = patientId,
                        profilePicture = bitmapToByteArray(bitmap),
                        name = name,
                        age = age.toInt(),
                        gender = gender,
                        cpf = cpf,
                        email = email,
                        telNumber = telNumber,
                        isDiabetic = isDiabetic,
                        hasHyperTension = hasHyperTension,
                        hasGlaucoma = hasGlaucoma,
                        description = description
                    )
                } else {
                    PatientData(
                        patientId = newPatientId,
                        profilePicture = bitmapToByteArray(bitmap),
                        name = name,
                        age = age.toInt(),
                        gender = gender,
                        cpf = cpf,
                        email = email,
                        telNumber = telNumber,
                        isDiabetic = isDiabetic,
                        hasHyperTension = hasHyperTension,
                        hasGlaucoma = hasGlaucoma,
                        description = description
                    )
                }


                viewModelScope.launch {
                    patientDataDao.upsertPatientData(patientData)
                    //por algum motivo esse hide dialog não funciona da forma que deveria, ainda mais aqui
                    onEvent(PatientDataEvent.HideDialog)
                }

            }

            is PatientDataEvent.SetPatientName -> {
                _patientDataState.update {
                    it.copy(
                        name = event.name
                    )
                }
            }

            is PatientDataEvent.SetPatientAge -> {
                _patientDataState.update {
                    it.copy(
                        age = event.age
                    )
                }
            }

            is PatientDataEvent.SetPatientGender -> {
                _patientDataState.update {
                    it.copy(
                        gender = event.gender
                    )
                }
            }

            is PatientDataEvent.SetPatientCPF -> {
                _patientDataState.update {
                    it.copy(
                        cpf = event.cpf
                    )
                }
            }

            is PatientDataEvent.SetPatientEmail -> {
                _patientDataState.update {
                    it.copy(
                        email = event.email
                    )
                }
            }

            is PatientDataEvent.SetPatientTelNumber -> {
                _patientDataState.update {
                    it.copy(
                        telNumber = event.telNumber
                    )
                }
            }

            is PatientDataEvent.SetIsDiabetic -> {
                _patientDataState.update {
                    it.copy(
                        isDiabetic = event.isDiabetic
                    )
                }
            }

            is PatientDataEvent.SetHasHyperTension -> {
                _patientDataState.update {
                    it.copy(
                        hasHyperTension = event.hasHyperTension
                    )
                }
            }

            is PatientDataEvent.SetHasGlaucoma -> {
                _patientDataState.update {
                    it.copy(
                        hasGlaucoma = event.hasGlaucoma
                    )
                }
            }

            is PatientDataEvent.SetDescription -> {
                _patientDataState.update {
                    it.copy(
                        description = event.description
                    )
                }
            }

            PatientDataEvent.ExpandGenderMenu -> {
                _patientDataState.update {
                    it.copy(
                        genderMenuExpanded = true
                    )
                }
            }

            PatientDataEvent.ShrinkGenderMenu -> {
                _patientDataState.update {
                    it.copy(
                        genderMenuExpanded = false
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