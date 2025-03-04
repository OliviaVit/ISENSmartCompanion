package fr.isen.ines.isensmartcompanion.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import fr.isen.vittenet.isensmartcompanion.components.AddLessonDialog
import fr.isen.vittenet.isensmartcompanion.data.LessonManager
import fr.isen.vittenet.isensmartcompanion.helpers.trieListeEvents
import fr.isen.vittenet.isensmartcompanion.helpers.trieListeLessons
import fr.isen.vittenet.isensmartcompanion.components.middleWithDays
import fr.isen.vittenet.isensmartcompanion.components.topWithMonth
import fr.isen.vittenet.isensmartcompanion.data.LessonDatabase
import fr.isen.vittenet.isensmartcompanion.models.EventModel
import fr.isen.vittenet.isensmartcompanion.models.LessonModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var lessons by remember { mutableStateOf<List<LessonModel>>(emptyList()) }
    var events by remember { mutableStateOf<List<EventModel>>(emptyList()) }
    var selectedDate = remember { mutableStateOf(LocalDate.now()) }
    var showDialog by remember { mutableStateOf(false) }
    val eventsWithDate = remember { mutableStateMapOf<LocalDate, List<EventModel>>() }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val daysInMonth = remember { mutableStateOf((1..currentMonth.lengthOfMonth()).map { currentMonth.atDay(it) }) }
    val lessonManager = LessonManager(LessonDatabase.getDatabase(context).lessonDao())

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            lessons = lessonManager.getAllLessons()
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
            modifier = Modifier.fillMaxWidth()
        ) {
            currentMonth = topWithMonth(daysInMonth)
        }
        selectedDate = middleWithDays(daysInMonth, eventsWithDate)

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            val filteredEvents = trieListeEvents(events, selectedDate.value)
            items(filteredEvents) { event ->
                Text(
                    text = event.title,
                    modifier = Modifier.padding(8.dp)
                )
            }
            val filteredLessons = trieListeLessons(lessons, selectedDate.value)
            items(filteredLessons) { lesson ->
                Row() {
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
                            lessonManager.deleteLesson(lesson.id)
                            lessons = lessonManager.getAllLessons()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Supprimer un cours"
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
                        lessonManager.insertLesson(lesson)
                        lessons = lessonManager.getAllLessons()
                    }
                    showDialog = false
                }
            )
        }
    }
}
