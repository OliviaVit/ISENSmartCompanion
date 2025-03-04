package fr.isen.vittenet.isensmartcompanion.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fr.isen.vittenet.isensmartcompanion.interfaces.LessonDao
import fr.isen.vittenet.isensmartcompanion.models.LessonModel

@Database(entities = [LessonModel::class], version = 1, exportSchema = false)
abstract class LessonDatabase : RoomDatabase() {
    abstract fun lessonDao(): LessonDao

    companion object {
        @Volatile
        private var INSTANCE: LessonDatabase? = null

        fun getDatabase(context: Context): LessonDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LessonDatabase::class.java,
                    "lesson_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}