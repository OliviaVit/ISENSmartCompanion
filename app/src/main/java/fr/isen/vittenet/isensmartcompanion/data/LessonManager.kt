package fr.isen.vittenet.isensmartcompanion.data

import android.content.Context
import fr.isen.vittenet.isensmartcompanion.interfaces.LessonDao
import fr.isen.vittenet.isensmartcompanion.models.LessonModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LessonManager(private val lessonDao: LessonDao) {

    suspend fun insertLesson(lesson: LessonModel) {
        withContext(Dispatchers.IO) {
            lessonDao.insertLesson(lesson)
        }
    }

    suspend fun insertAll(lessons: List<LessonModel>) {
        withContext(Dispatchers.IO) {
            lessonDao.insertAll(lessons)
        }
    }

    suspend fun getAllLessons(): List<LessonModel> {
        return withContext(Dispatchers.IO) {
            lessonDao.getAllLessons()
        }
    }

    suspend fun deleteLesson(lessonId: Int) {
        withContext(Dispatchers.IO) {
            lessonDao.deleteLesson(lessonId)
        }
    }

    suspend fun deleteAllLessons() {
        withContext(Dispatchers.IO) {
            lessonDao.deleteAllLesson()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LessonManager? = null

        fun getInstance(context: Context): LessonManager {
            return INSTANCE ?: synchronized(this) {
                val database = LessonDatabase.getDatabase(context)
                val instance = LessonManager(database.lessonDao())
                INSTANCE = instance
                instance
            }
        }
    }
}
