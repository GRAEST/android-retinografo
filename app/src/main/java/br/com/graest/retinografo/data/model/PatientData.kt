package br.com.graest.retinografo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (
    tableName = "patient_data"
)
data class PatientData(
    val name: String,
    val age: Int = 0,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
