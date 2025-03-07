package fr.isen.vittenet.isensmartcompanion.helpers


import android.os.Build
import androidx.annotation.RequiresApi
import fr.isen.vittenet.isensmartcompanion.models.EventModel
import fr.isen.vittenet.isensmartcompanion.models.LessonModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.TextStyle
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
fun trieListeEvents(events: List<EventModel>, selectedDate: LocalDate): List<EventModel> {
    return events.filter { event ->
        val eventDate = parseDate(event.date)
        eventDate == selectedDate
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun trieListeLessons(lessons: List<LessonModel>, selectedDate: LocalDate): List<LessonModel> {
    val selectedDayOfWeek = selectedDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
    return lessons.filter { lesson ->
        if (lesson.isRecurrent) {
            lesson.day.split(", ").any { it.equals(selectedDayOfWeek, ignoreCase = true) }
        } else {
            lesson.day == selectedDate.toString()
        }
    }.sortedBy { lesson ->
        try {
            LocalTime.parse(lesson.time, DateTimeFormatter.ofPattern("H:mm"))
        } catch (e: DateTimeParseException) {
            LocalTime.MIN
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun parseDate(dateString: String): LocalDate {
    val cleanDateString = dateString.replaceFirst("^([0-9]+)er\\s".toRegex(), "$1 ")
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRENCH)

    return try {
        LocalDate.parse(cleanDateString.trim(), formatter)
    } catch (e: Exception) {
        throw DateTimeParseException("Format error in date : $dateString", dateString, 0)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getEventDates(events: List<EventModel>): List<LocalDate> {
    return events.mapNotNull { event ->
        try {
            parseDate(event.date)
        } catch (e: Exception) {
            println("Conversion error in date : ${event.date}")
            null
        }
    }.distinct()
}

