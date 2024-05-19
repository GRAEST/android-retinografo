package br.com.graest.retinografo.ui.screens.patient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.graest.retinografo.data.local.PatientDataDao
import br.com.graest.retinografo.data.model.PatientData
import br.com.graest.retinografo.data.model.SortPatientType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class PatientDataViewModel(
    private val patientDataDao: PatientDataDao
) : ViewModel() {

    private val _sortPatientDataType = MutableStateFlow(SortPatientType.PATIENT_AGE)


    private val _patientData = _sortPatientDataType
        .flatMapLatest { sortPatientType ->
            when(sortPatientType) {
                SortPatientType.PATIENT_AGE -> patientDataDao.getPatientDataOrderedByAge()
                SortPatientType.PATIENT_NAME -> patientDataDao.getPatientDataOrderedByName()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _patientDataState = MutableStateFlow(PatientDataState())
    val patientDataState = combine(_patientDataState, _sortPatientDataType, _patientData) { patientState, sortPatientDataType, patientData ->
        patientState.copy (
            patientData = patientData,
            sortPatientDataType = sortPatientDataType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PatientDataState())

    fun onEvent(event: PatientDataEvent) {
        when(event) {
            is PatientDataEvent.DeletePatientData -> {
                viewModelScope.launch {
                    patientDataDao.deletePatientData(event.patientData)
                }
            }
            PatientDataEvent.HideDialog -> {
                _patientDataState.update { it.copy(
                    isAddingPatientData = false
                ) }
            }
            PatientDataEvent.SavePatientData -> {
                val age = patientDataState.value.age
                val name = patientDataState.value.name

                if (age.isBlank() || name.isBlank()) {
                    return
                }

                val patientData = PatientData(
                    age = age.toInt(),
                    name = name
                )

                viewModelScope.launch{
                    patientDataDao.upsertPatientData(patientData)
                }
                _patientDataState.update { it.copy(
                    isAddingPatientData = false,
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
            PatientDataEvent.ShowDialog -> {
                _patientDataState.update { it.copy(
                    isAddingPatientData = true
                ) }
            }
            is PatientDataEvent.SortPatientData -> {
                _sortPatientDataType.value = event.sortPatientType
            }
            else -> {}
        }
    }
}