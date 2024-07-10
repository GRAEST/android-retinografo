package br.com.graest.retinografo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import br.com.graest.retinografo.data.model.ExamData
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamDataDao {
    //ver se da para diminuir esse nome gigantesco antes de ExamData
    @Insert
    suspend fun insertExam(image: ExamData): Long

    @Query("SELECT * FROM exam_data ORDER BY id ASC")
    fun getExamData() : Flow<List<ExamData>>

//    @Query("SELECT * FROM exam_data WHERE id = :id")
//    suspend fun getExamById(id: Int): Flow<ExamData?>

    @Query("DELETE FROM exam_data WHERE id = :id")
    suspend fun deleteExamById(id: Int)

    @Delete
    suspend fun deleteExamData(examData: ExamData)
}