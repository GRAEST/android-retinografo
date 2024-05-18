package br.com.graest.retinografo.data.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.graest.retinografo.data.model.PatientData
import br.com.graest.retinografo.data.local.PatientDataDao

@Database(
    entities = [PatientData::class],
    version = 1
)
abstract class Database : RoomDatabase() {

    abstract val dao: PatientDataDao

}