package br.com.graest.retinografo.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class ExamDataWithPatient(
    @Embedded val examData: ExamData,
    @Relation(
        parentColumn = "patientId",
        entityColumn = "patientId"
    )
    val patientData: PatientData
)