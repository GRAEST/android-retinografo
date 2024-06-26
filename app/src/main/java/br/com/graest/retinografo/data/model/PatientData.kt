package br.com.graest.retinografo.data.model

import androidx.compose.ui.res.painterResource
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.graest.retinografo.R

@Entity (
    tableName = "patient_data"
)
data class PatientData(
    val name: String,
    val age: Int = 0,
    val image: ByteArray = ByteArray(1),
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
