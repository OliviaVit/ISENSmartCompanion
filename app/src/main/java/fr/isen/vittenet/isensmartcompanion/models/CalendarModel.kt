package fr.isen.vittenet.isensmartcompanion.models

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.media.metrics.Event
import android.os.Build
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.isen.ines.isensmartcompanion.screens.EventItem
import fr.isen.vittenet.isensmartcompanion.Greeting
import fr.isen.vittenet.isensmartcompanion.R
import fr.isen.vittenet.isensmartcompanion.data.AppDatabase
import fr.isen.vittenet.isensmartcompanion.data.LessonDatabase
import fr.isen.vittenet.isensmartcompanion.screens.eventService
import fr.isen.vittenet.isensmartcompanion.screens.ui.theme.ISENSmartCompanionTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarModel() {

    val baseLessons = listOf(
        LessonModel(1,"Mathématiques", "Cours de mathématiques avancées", "Lundi", "Salle 101", "08:00",true),
        LessonModel(2,"Physique", "Cours de physique sur la mécanique quantique", "Mardi", "Salle 202", "10:00",true),
        LessonModel( 3,"Informatique", "Introduction à la programmation en Kotlin", "Mercredi", "Salle 303", "14:00",true),
        LessonModel( 4,"Chimie", "Cours de chimie organique", "Jeudi", "Salle 404", "09:00",true),
        LessonModel(5,"Histoire", "Étude des grandes civilisations", "Vendredi", "Salle 505", "11:00",true),
        LessonModel( 6,"Anglais", "Cours d'anglais avancé", "Lundi", "Salle 606", "13:00",true),
        LessonModel( 7,"Électronique", "Introduction aux circuits électriques", "Mardi", "Salle 707", "15:00",true),
        LessonModel( 8,"Philosophie", "Philosophie des sciences", "Mercredi", "Salle 808", "16:00",true),
        LessonModel( 9,"Sport", "Activités sportives et entraînement", "Jeudi", "Salle de sport", "17:00",true),
        LessonModel( 10,"Biologie", "Cours sur la génétique et l'évolution", "Vendredi", "Salle 909", "18:00",true)
    )


    var lessons by remember { mutableStateOf<List<LessonModel>>(emptyList()) }

    val context = LocalContext.current
    val eventsWithDate = remember { mutableStateMapOf<LocalDate, MutableList<EventModel>>() }

    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val daysInMonth = remember { mutableStateOf((1..currentMonth.lengthOfMonth()).map { currentMonth.atDay(it) }) }

    var selectedDate = remember { mutableStateOf(LocalDate.now()) }
    var events by remember { mutableStateOf<List<EventModel>>(emptyList()) }

    var showDialog by remember { mutableStateOf(false) }
    val database = LessonDatabase.getDatabase(context)

    val coroutineScope = rememberCoroutineScope()

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

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            if (database.lessonDao().getAllLessons().isEmpty()) {
                database.lessonDao().insertAll(baseLessons)
            }
            lessons = database.lessonDao().getAllLessons()
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
            val eventOk = trieListeEvents(events,selectedDate.value)
            items(eventOk) { event ->
                Text(
                    text = event.title,
                    modifier = Modifier.padding(8.dp)
                )
            }
            val lessonOk = trieListeLessons(lessons,selectedDate.value)
            items(lessonOk) { lesson ->
                Row(){
                    Text(
                        text = lesson.time,
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        text = lesson.title,
                        modifier = Modifier.padding(8.dp)
                    )
                    IconButton(onClick = {
                        coroutineScope.launch {
                            database.lessonDao().deleteLesson(lesson.id)
                            lessons = database.lessonDao().getAllLessons()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Supp un cours"
                        )
                    }
                }

            }

        }
        IconButton(onClick = {
            showDialog = true
        }) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Ajouter un cours"
            )
        }

        if (showDialog) {
            AddLessonDialog(
                onDismiss = { showDialog = false },
                onAddLesson = { lesson ->
                    coroutineScope.launch {
                        database.lessonDao().insertLesson(lesson)
                        lessons = database.lessonDao().getAllLessons()
                    }
                    showDialog = false
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddLessonDialog(onDismiss: () -> Unit, onAddLesson: (LessonModel) -> Unit) {
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var location by remember { mutableStateOf(TextFieldValue("")) }
    var time by remember { mutableStateOf<LocalTime?>(null) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) } // Pour événement unique

    val context = LocalContext.current

    // Choix entre événement unique ou récurrent
    var isRecurrent by remember { mutableStateOf(true) }

    // Liste des jours de la semaine
    val joursSemaine = listOf("Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche")
    val selectedDays = remember { mutableStateMapOf<String, Boolean>().apply { joursSemaine.forEach { put(it, false) } } }

    // TimePickerDialog pour sélectionner l'heure
    val timePickerDialog = remember {
        TimePickerDialog(
            context,
            { _: TimePicker, hour: Int, minute: Int ->
                time = LocalTime.of(hour, minute)
            },
            12, // Heure par défaut
            0,  // Minute par défaut
            true // Format 24h
        )
    }

    // DatePickerDialog pour sélectionner une date spécifique
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                selectedDate = LocalDate.of(year, month + 1, dayOfMonth) // Mois commence à 0 en Java
            },
            LocalDate.now().year,
            LocalDate.now().monthValue - 1,
            LocalDate.now().dayOfMonth
        )
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Ajouter un cours") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Titre") }
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Salle") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Sélection entre événement récurrent ou unique
                Row {
                    Button(onClick = { isRecurrent = true }, enabled = !isRecurrent) {
                        Text("Événement récurrent")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { isRecurrent = false }, enabled = isRecurrent) {
                        Text("Événement unique")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Si événement récurrent, afficher les jours de la semaine
                if (isRecurrent) {
                    Column {
                        Text("Choisissez les jours :", style = MaterialTheme.typography.bodyMedium)
                        joursSemaine.forEach { jour ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .toggleable(
                                        value = selectedDays[jour] == true,
                                        onValueChange = { selectedDays[jour] = it }
                                    )
                            ) {
                                Checkbox(
                                    checked = selectedDays[jour] == true,
                                    onCheckedChange = { selectedDays[jour] = it }
                                )
                                Text(jour, modifier = Modifier.padding(start = 8.dp))
                            }
                        }
                    }
                }
                // Si événement unique, afficher le bouton pour sélectionner une date
                else {
                    Column {
                        Button(onClick = { datePickerDialog.show() }) {
                            Text("Sélectionner une date")
                        }
                        selectedDate?.let {
                            Text(
                                text = "Date sélectionnée : ${it.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Bouton pour ouvrir le TimePickerDialog
                Button(onClick = { timePickerDialog.show() }) {
                    Text("Sélectionner une heure")
                }

                // Affichage de l'heure sélectionnée
                time?.let {
                    Text(
                        text = "Heure sélectionnée : ${it.toString()}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val selectedDaysList = if (isRecurrent) selectedDays.filterValues { it }.keys.joinToString(", ") else ""

                    val lesson = LessonModel(
                        id = (1..1000).random(),
                        title = title.text,
                        description = description.text,
                        day = if (isRecurrent) selectedDaysList else selectedDate?.toString() ?: "", // Utilisation de la date si événement unique
                        location = location.text,
                        time = time?.toString() ?: "00:00",
                        isRecurrent = isRecurrent
                    )
                    onAddLesson(lesson)
                }
            ) {
                Text("Ajouter")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Annuler")
            }
        }
    )
}
@RequiresApi(Build.VERSION_CODES.O)
fun trieListeEvents(events: List<EventModel>, selectedDate: LocalDate): List<EventModel> {
    return events.filter { event ->
        val eventDate = parseDate(event.date)
        eventDate == selectedDate
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun trieListeLessons(lessons: List<LessonModel>, selectedDate: LocalDate): List<LessonModel> {
    val selectedDayOfWeek = selectedDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.FRANCE)

    return lessons.filter { lesson ->
        if (lesson.isRecurrent) {
            // Vérifier si le jour de la semaine de la date sélectionnée correspond à l'un des jours stockés
            lesson.day.split(", ").any { it.equals(selectedDayOfWeek, ignoreCase = true) }
        } else {
            // Vérifier si la date stockée correspond exactement à la date sélectionnée (événement unique)
            lesson.day == selectedDate.toString()
        }
    }.sortedBy { lesson ->
        try {
            LocalTime.parse(lesson.time, DateTimeFormatter.ofPattern("H:mm"))
        } catch (e: DateTimeParseException) {
            LocalTime.MIN // Si erreur, positionne en premier
        }
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