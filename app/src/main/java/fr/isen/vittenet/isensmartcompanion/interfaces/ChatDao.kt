package fr.isen.vittenet.isensmartcompanion.interfaces
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.isen.vittenet.isensmartcompanion.models.ChatModel


@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chatMessage: ChatModel)

    @Query("SELECT * FROM chat_messages ORDER BY timestamp DESC")
    suspend fun getAllChats(): List<ChatModel>

    @Query("DELETE FROM chat_messages WHERE id = :chatId")
    suspend fun deleteChat(chatId: Int)

    @Query("DELETE FROM chat_messages")
    suspend fun deleteAllChats()


}




