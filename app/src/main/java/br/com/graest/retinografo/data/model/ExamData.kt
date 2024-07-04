package br.com.graest.retinografo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "exam_data"
)
data class ExamData (
    val image: ByteArray,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = 0
)