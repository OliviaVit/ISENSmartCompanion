package fr.isen.vittenet.isensmartcompanion.data

import fr.isen.vittenet.isensmartcompanion.interfaces.EventService
import fr.isen.vittenet.isensmartcompanion.models.EventModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object EventManager {
    private const val BASE_URL = "https://isen-smart-companion-default-rtdb.europe-west1.firebasedatabase.app/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val eventService: EventService = retrofit.create(EventService::class.java)

    suspend fun getEvents(): List<EventModel> {
        return try {
            eventService.getEvents()
        } catch (e: Exception) {
            emptyList()
        }
    }
}