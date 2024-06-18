package br.com.graest.retinografo.ui.screens.patient

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.graest.retinografo.data.local.PatientDataDao
import br.com.graest.retinografo.data.model.PatientData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
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

    val patientDataState = combine(_patientDataState,  _patientsData) { patientState, patientsData->
        patientState.copy (
            patientsData = patientsData
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PatientDataState())

    fun onEvent(event: PatientDataEvent) {
        when(event) {
            is PatientDataEvent.DeletePatientData -> {

                Log.d("TAG", "Delete: " +
                        "${_patientDataState.value.id} + ${_patientDataState.value.name} + ${_patientDataState.value.age}"
                )

                viewModelScope.launch {
                    patientDataDao.deletePatientData(event.id)
                }
                _patientDataState.update { it.copy(
                    isEditingPatientData = false,
                    id = 0,
                    name = "",
                    age = ""
                ) }
                //quando o id deixa de existir, o state que lia do sql crasha

            }
            PatientDataEvent.HideDialog -> {

                Log.d("TAG", "Hide Add: " +
                        "${_patientDataState.value.id} + ${_patientDataState.value.name} + ${_patientDataState.value.age}"
                )

                _patientDataState.update { it.copy(
                    isAddingPatientData = false,
                    isEditingPatientData = false,
                    id = 0,
                    name = "",
                    age = ""
                ) }
            }
            PatientDataEvent.ShowAddPatientDialog -> {
                _patientDataState.update { it.copy(
                    isAddingPatientData = true
                ) }
                Log.d("TAG", "Show Add: " +
                        "${_patientDataState.value.id} + ${_patientDataState.value.name} + ${_patientDataState.value.age}"
                )
            }
            is PatientDataEvent.ShowEditPatientDialog -> {
                viewModelScope.launch {
                    patientDataDao.getPatientData(event.id).collect { data ->
                        _patientDataState.update { currentState ->
                            currentState.copy(
                                isEditingPatientData = true,
                                id = event.id,
                                name = data.name,
                                age = data.age.toString()
                            )
                        }
                    }
                    Log.d("TAG", "Show Edit: " +
                            "${_patientDataState.value.id} + ${_patientDataState.value.name} + ${_patientDataState.value.age}"
                    )
                }
            }

            PatientDataEvent.SavePatientData -> {
                val id = patientDataState.value.id
                val age = patientDataState.value.age
                val name = patientDataState.value.name

                if (age.isBlank() || name.isBlank()) {
                    return
                }

                val patientData = PatientData(
                    id = id,
                    age = age.toInt(),
                    name = name
                )

                Log.d("TAG", "Save: " +
                        "${_patientDataState.value.id} + ${_patientDataState.value.name} + ${_patientDataState.value.age}"
                )

                viewModelScope.launch{
                    patientDataDao.upsertPatientData(patientData)
                }

                _patientDataState.update { it.copy(
                    id = 0,
                    name = "",
                    age = ""
                ) }
            }
            is PatientDataEvent.SetPatientAge -> {
                _patientDataState.update { it.copy(
                    age = event.age
                ) }
            }
            is PatientDataEvent.SetPatientName -> {
                _patientDataState.update { it.copy(
                    name = event.name
                ) }
            }
            else -> {}
        }
    }
}