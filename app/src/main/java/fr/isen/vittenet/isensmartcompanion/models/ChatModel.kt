package fr.isen.vittenet.isensmartcompanion.models


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val question: String,
    val answer: String,
    val timestamp: Long = System.currentTimeMillis()
)