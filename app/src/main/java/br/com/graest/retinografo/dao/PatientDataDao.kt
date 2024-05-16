package br.com.graest.retinografo.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import br.com.graest.retinografo.data.PatientData
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDataDao {

    @Upsert
    suspend fun upsertPatientData(patientData: PatientData)

    @Delete
    suspend fun deletePatientData(patientData: PatientData)

    @Query("SELECT * FROM patient_data ORDER BY age ASC")
    fun getPatientDataOrderedByAge() : Flow<List<PatientData>>

    @Query("SELECT * FROM patient_data ORDER BY name ASC")
    fun getPatientDataOrderedByName() : Flow<List<PatientData>>

}