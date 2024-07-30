package br.com.graest.retinografo.ui.screens.patient

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.graest.retinografo.data.local.PatientDataDao
import br.com.graest.retinografo.data.model.Gender
import br.com.graest.retinografo.data.model.PatientData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.nio.ByteBuffer
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
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
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), PatientDataState())

    /* O MUTABLE STATE PERSISTE DADOS MESMO QUANDO EU SOBRESCREVO, OU SEJA, QUANDO TEM DELETE, EDIT OU ADD, DA PROBLEMA */

    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: PatientDataEvent) {
        when (event) {

            is PatientDataEvent.DeletePatientDataById -> {
                viewModelScope.launch {
                    patientDataDao.deletePatientDataById(event.id)
                    closeDialogs()
                    clearStates()
                }

            }

            PatientDataEvent.HideDialog -> {
                viewModelScope.launch {
                    closeDialogs()
                    clearStates()
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
                clearStates()
                _patientDataState.update {
                    it.copy(
                        isAddingPatientData = true,
                    )
                }
            }

            is PatientDataEvent.ShowEditPatientDialog -> {
                viewModelScope.launch {
                    patientDataDao.getPatientData(event.id).collect { data ->
                        if (data != null) {
                            val birthDate = if (data.birthDate == null) {
                                ""
                            } else {
                                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                                data.birthDate.format(formatter)
                            }
                            _patientDataState.update { currentState ->
                                currentState.copy(
                                    isEditingPatientData = true,
                                    patientId = event.id,
                                    name = data.name,
                                    profilePicturePath = data.profilePicturePath,
                                    birthDate = birthDate,
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

                val patientId = patientDataState.value.patientId //id é necessário para caso de EDIT
                val profilePicturePath = patientDataState.value.profilePicturePath
                val name = patientDataState.value.name
                val gender = patientDataState.value.gender
                val cpf = patientDataState.value.cpf
                val email = patientDataState.value.email
                val telNumber = patientDataState.value.telNumber
                val isDiabetic = patientDataState.value.isDiabetic
                val hasHyperTension = patientDataState.value.hasHyperTension
                val hasGlaucoma = patientDataState.value.hasGlaucoma
                val description = patientDataState.value.description


                val birthDate = validateAndSetBirthDate(patientDataState.value.birthDate)

                val newPatientId = patientId ?: ByteBuffer.wrap(ByteArray(16))
                    .putLong(UUID.randomUUID().mostSignificantBits)
                    .putLong(UUID.randomUUID().leastSignificantBits)
                    .array()

                if (name.isBlank() || patientDataState.value.errorMessageBirthDate != "") {
                    return
                }

                val patientData = if (patientId != null) {
                    PatientData(
                        patientId = patientId,
                        profilePicturePath = profilePicturePath,
                        name = name,
                        birthDate = birthDate,
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
                        profilePicturePath = profilePicturePath,
                        name = name,
                        birthDate = birthDate,
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
                    clearStates()
                    closeDialogs()
                }
            }

            is PatientDataEvent.SetPatientName -> {
                _patientDataState.update {
                    it.copy(
                        name = event.name
                    )
                }
            }

            is PatientDataEvent.SetPatientBirthDate -> {
                _patientDataState.update {
                    it.copy(
                        birthDate = event.birthDate
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

    // to kill all problems, try and avoid cuncurrency problems (async and sync) so move all to async
    fun resetFlowState() {
        viewModelScope.launch {
            _patientDataState.value = PatientDataState()
        }
    }


    fun imageAlreadyEdited() {
        _patientDataState.update {
            it.copy(
                isEditingImage = false,
            )
        }
    }

    private fun closeDialogs() {
        _patientDataState.update {
            it.copy(
                isAddingPatientData = false,
                isEditingPatientData = false
            )
        }
    }

    private fun clearStates() {
        _patientDataState.update {
            it.copy(
                patientId = null,
                name = "",
                birthDate = "",
                errorMessageBirthDate = "",
                gender = Gender.OTHER,
                cpf = "",
                email = "",
                telNumber = "",
                isDiabetic = false,
                hasHyperTension = false,
                hasGlaucoma = false,
                description = "",
                profilePicturePath = "",
            )
        }
    }

    fun setCapturedImagePath(path: String) {
        _patientDataState.update {
            it.copy(
                profilePicturePath = path
            )
        }
    }

    fun setErrorMessage(message: String?) {
        _patientDataState.update {
            it.copy(
                fileImageErrorMessage = message
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun validateAndSetBirthDate(
        birthDateString: String,
    ): LocalDate? {
        return try {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val localDate = LocalDate.parse(birthDateString, formatter)
            val stringDate = localDate.format(formatter)
            onEvent(PatientDataEvent.SetPatientBirthDate(stringDate))
            _patientDataState.update {
                it.copy(
                    errorMessageBirthDate = ""
                )
            }
            localDate
        } catch (e: DateTimeParseException) {
            _patientDataState.update {
                it.copy(
                    errorMessageBirthDate = "Invalid date format. Please use dd-MM-yyyy."
                )
            }
            null
        }
    }

}