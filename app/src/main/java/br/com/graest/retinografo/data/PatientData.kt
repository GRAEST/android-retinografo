package br.com.graest.retinografo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (
    tableName = "patient_data"
)
data class PatientData(
    val name: String,
    val age: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
