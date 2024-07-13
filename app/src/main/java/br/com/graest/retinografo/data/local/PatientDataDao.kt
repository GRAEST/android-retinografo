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

    @Query("DELETE FROM patient_data WHERE patientId = :patientId")
    suspend fun deletePatientDataById(patientId: Int)

    @Query("SELECT * FROM patient_data WHERE patientId = :patientId")
    fun getPatientData(patientId: Int) : Flow<PatientData?>

    @Query("SELECT * FROM patient_data ORDER BY name ASC")
    fun getPatientsData() : Flow<List<PatientData?>>

}