package fr.isen.vittenet.isensmartcompanion.interfaces

import fr.isen.vittenet.isensmartcompanion.models.EventModel
import retrofit2.http.GET

interface EventService {
    @GET("events.json")
    suspend fun getEvents(): List<EventModel>
}