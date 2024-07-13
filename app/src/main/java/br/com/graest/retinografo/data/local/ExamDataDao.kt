package br.com.graest.retinografo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.graest.retinografo.data.model.ExamData
import br.com.graest.retinografo.data.model.ExamDataWithPatient
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamDataDao {
    @Insert
    suspend fun insertExam(examData: ExamData): Long

//    @Query("SELECT * FROM exam_data ORDER BY id ASC")
//    fun getExamData() : Flow<List<ExamData>>

//    @Query("SELECT * FROM exam_data WHERE id = :id")
//    suspend fun getExamById(id: Int): Flow<ExamData?>

    @Query("SELECT exam_data.*, patient_data.* FROM exam_data INNER JOIN patient_data ON exam_data.patientId = patient_data.patientId ORDER BY exam_data.id ASC")
    fun getExamDataWithPatients(): Flow<List<ExamDataWithPatient>>

    @Query("DELETE FROM exam_data WHERE id = :id")
    suspend fun deleteExamById(id: Int)
}