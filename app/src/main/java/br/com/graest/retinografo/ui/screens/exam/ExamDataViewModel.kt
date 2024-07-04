package br.com.graest.retinografo.ui.screens.exam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.graest.retinografo.data.local.ExamDataDao
import br.com.graest.retinografo.data.model.ExamData
import br.com.graest.retinografo.ui.screens.patient.PatientDataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExamDataViewModel(
    private val examDataDao: ExamDataDao
) : ViewModel() {

    private val _examsData = examDataDao.getExamData()

    private val _examDataState = MutableStateFlow(ExamDataState())

    val examDataState = combine(_examDataState, _examsData) { examDataState, examsData ->
        examDataState.copy(
            examsData = examsData
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExamDataState())

    fun onEvent(event: ExamDataEvent) {
        when (event) {
            ExamDataEvent.DeleteExamData -> {
                val examData = ExamData(
                    image = examDataState.value.image
                )
                viewModelScope.launch {
                    examDataDao.deleteExamData(examData)
                }
            }
            is ExamDataEvent.DeleteExamDataById -> {
                viewModelScope.launch {
                    examDataDao.deleteExamById(event.id)
                }
            }
            ExamDataEvent.SaveExamData -> {

            }
            is ExamDataEvent.SetExamImage -> {
                viewModelScope.launch {
                    //examDataDao.insertExam(event.image)
                }
            }
            else ->  { }
        }
    }


}