package fr.isen.vittenet.isensmartcompanion.models

import android.media.metrics.Event
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.isen.ines.isensmartcompanion.screens.EventItem
import fr.isen.vittenet.isensmartcompanion.Greeting
import fr.isen.vittenet.isensmartcompanion.R
import fr.isen.vittenet.isensmartcompanion.screens.eventService
import fr.isen.vittenet.isensmartcompanion.screens.ui.theme.ISENSmartCompanionTheme
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarModel() {
    val context = LocalContext.current
    val eventsWithDate = remember { mutableStateMapOf<LocalDate, MutableList<EventModel>>() }
    var newEvent by remember { mutableStateOf(TextFieldValue("")) }

    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val daysInMonth = remember { mutableStateOf((1..currentMonth.lengthOfMonth()).map { currentMonth.atDay(it) }) }

    var selectedDate = remember { mutableStateOf(LocalDate.now()) }
    var eventsForSelectedDate by remember { mutableStateOf<List<EventModel>>(emptyList()) }
    var events by remember { mutableStateOf<List<EventModel>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            val fetchedEvents = eventService.getEvents()
            events = fetchedEvents
            eventsWithDate.clear()
            fetchedEvents.forEach { event ->
                val eventDate = parseDate(event.date)
                eventsWithDate.getOrPut(eventDate) { mutableListOf() }.add(event)
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error fetching events: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
        ) {
            currentMonth = topWithMonth(daysInMonth)
        }
        selectedDate = middleWithDays(daysInMonth, eventsWithDate)
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            val eventOk = trieListe(events,selectedDate.value)
            items(eventOk) { event ->
                Text(
                    text = event.title,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun trieListe(events: List<EventModel>, selectedDate: LocalDate): List<EventModel> {
    return events.filter { event ->
        val eventDate = parseDate(event.date)
        eventDate == selectedDate
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun middleWithDays(daysInMonth: MutableState<List<LocalDate>>, eventsWithDate: SnapshotStateMap<LocalDate, MutableList<EventModel>>): MutableState<LocalDate> {
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(daysInMonth.value.size) { index ->
            val day = daysInMonth.value[index]
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        if (day == selectedDate.value) colorResource(R.color.institutionnel_color) else Color.Transparent,
                        shape = RoundedCornerShape(50)
                    )
                    .clickable { selectedDate.value = day },
                contentAlignment = Alignment.Center
            ) {
                if (eventsWithDate.containsKey(day)) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(Color.Red, shape = RoundedCornerShape(50))
                    )
                }
                Text(text = day.dayOfMonth.toString(), color = Color.Black)
            }
        }
    }

    return selectedDate
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun topWithMonth(daysInMonth: MutableState<List<LocalDate>>): YearMonth? {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    IconButton(onClick = {
        currentMonth = currentMonth.minusMonths(1)
        daysInMonth.value = (1..currentMonth.lengthOfMonth()).map { currentMonth.atDay(it) }
    }) {
        Icon(Icons.Filled.ArrowBack, contentDescription = "Mois précédent")
    }
    Text(currentMonth.month.name + " " + currentMonth.year)
    IconButton(onClick = {
        currentMonth = currentMonth.plusMonths(1)
        daysInMonth.value = (1..currentMonth.lengthOfMonth()).map { currentMonth.atDay(it) }
    }) {
        Icon(Icons.Filled.ArrowForward, contentDescription = "Mois suivant")
    }
    return currentMonth
}


@RequiresApi(Build.VERSION_CODES.O)
fun parseDate(dateString: String): LocalDate {
    val cleanDateString = dateString.replaceFirst("^([0-9]+)er\\s".toRegex(), "$1 ") // Remplace "1er" par "1"

    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRENCH)

    return try {
        LocalDate.parse(cleanDateString.trim(), formatter)
    } catch (e: Exception) {
        throw DateTimeParseException("Erreur de format de date : $dateString", dateString, 0)
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    ISENSmartCompanionTheme {
        CalendarModel()
    }
}
