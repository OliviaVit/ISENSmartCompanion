package fr.isen.vittenet.isensmartcompanion.models

data class EventModel(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val category: String
) {
}