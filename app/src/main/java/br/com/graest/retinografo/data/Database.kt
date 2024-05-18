package br.com.graest.retinografo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.graest.retinografo.data.patient.PatientData
import br.com.graest.retinografo.data.patient.PatientDataDao

@Database(
    entities = [PatientData::class],
    version = 1
)
abstract class Database : RoomDatabase() {

    abstract val dao: PatientDataDao

}