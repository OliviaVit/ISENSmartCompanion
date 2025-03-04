package fr.isen.vittenet.isensmartcompanion.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "lessons")
data class LessonModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val day: String,
    val location: String,
    val time: String,
    val isRecurrent: Boolean
) : Serializable