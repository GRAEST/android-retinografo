package br.com.graest.retinografo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import br.com.graest.retinografo.data.model.PatientData
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDataDao {

    @Upsert
    suspend fun upsertPatientData(patientData: PatientData)

    @Delete
    suspend fun deletePatientData(patientData: PatientData)

    @Query("DELETE FROM patient_data WHERE id = :id")
    suspend fun deletePatientDataById(id: Int)

    @Query("SELECT * FROM patient_data WHERE id = :id")
    fun getPatientData(id: Int) : Flow<PatientData?>

    @Query("SELECT * FROM patient_data ORDER BY name ASC")
    fun getPatientsData() : Flow<List<PatientData?>>

}