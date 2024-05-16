package br.com.graest.retinografo

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.graest.retinografo.dao.PatientDataDao
import br.com.graest.retinografo.data.PatientData

@Database(
    entities = [PatientData::class],
    version = 1
)
abstract class PatientDatabase : RoomDatabase() {

    abstract val dao: PatientDataDao

}