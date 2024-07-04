package br.com.graest.retinografo.data.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.graest.retinografo.data.local.ExamDataDao
import br.com.graest.retinografo.data.model.PatientData
import br.com.graest.retinografo.data.local.PatientDataDao
import br.com.graest.retinografo.data.model.ExamData

@Database(
    entities = [PatientData::class, ExamData::class],
    version = 1
)
abstract class Database : RoomDatabase() {

    abstract val patientDataDao: PatientDataDao
    abstract val examDataDao: ExamDataDao

}