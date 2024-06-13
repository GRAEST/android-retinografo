package br.com.graest.retinografo.ui.screens.patient

import androidx.camera.core.impl.CameraRepository
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.graest.retinografo.data.local.PatientDataDao
import br.com.graest.retinografo.data.model.PatientData
import br.com.graest.retinografo.data.model.SortPatientType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class PatientDataViewModel(
    private val patientDataDao: PatientDataDao
) : ViewModel() {

    private var _patientData = MutableStateFlow<PatientData?>(null)
    val patientData: StateFlow<PatientData?> get() = _patientData

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
                viewModelScope.launch {
                    patientDataDao.deletePatientData(event.patientData)
                }
            }
            PatientDataEvent.HideAddPatientDialog -> {
                _patientDataState.update { it.copy(
                    isAddingPatientData = false
                ) }
            }
            PatientDataEvent.ShowAddPatientDialog -> {
                _patientDataState.update { it.copy(
                    isAddingPatientData = true
                ) }
            }
            PatientDataEvent.HideEditPatientDialog -> {
                _patientDataState.update { it.copy(
                    isEditingPatientData = false
                ) }
            }
            is PatientDataEvent.ShowEditPatientDialog -> {
                // criar um método para pegar os valores correpondentes ao id enviado pelo PatientScreen

                viewModelScope.launch {
                    patientDataDao.getPatientData(event.id).collect {data ->
                        _patientData.value = data
                    }
                }

                // criar um método para preencher os valores de name e age com os do sql correspondente
                _patientDataState.update { it.copy(
                    name = patientData.name,
                    age = patientData.age
                ) }

                //mostrar a tela em questão
                _patientDataState.update { it.copy(
                    isEditingPatientData = true
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
                    isEditingPatientData = false,
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
        }
    }
}