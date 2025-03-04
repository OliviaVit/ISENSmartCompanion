package fr.isen.vittenet.isensmartcompanion.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.isen.vittenet.isensmartcompanion.models.LessonModel

@Dao
interface LessonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLesson(lesson: LessonModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(lessons: List<LessonModel>)

    @Query("SELECT * FROM lessons")
    suspend fun getAllLessons(): List<LessonModel>

    @Query("DELETE FROM lessons WHERE id = :lessonId")
    suspend fun deleteLesson(lessonId: Int)

}