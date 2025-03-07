package fr.isen.vittenet.isensmartcompanion.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.vittenet.isensmartcompanion.R
import fr.isen.vittenet.isensmartcompanion.components.AddLessonDialog
import fr.isen.vittenet.isensmartcompanion.data.LessonManager
import fr.isen.vittenet.isensmartcompanion.helpers.trieListeEvents
import fr.isen.vittenet.isensmartcompanion.helpers.trieListeLessons
import fr.isen.vittenet.isensmartcompanion.components.middleWithDays
import fr.isen.vittenet.isensmartcompanion.components.topWithMonth
import fr.isen.vittenet.isensmartcompanion.data.EventManager
import fr.isen.vittenet.isensmartcompanion.data.LessonDatabase
import fr.isen.vittenet.isensmartcompanion.helpers.getEventDates
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
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val daysInMonth = remember { mutableStateOf((1..currentMonth.lengthOfMonth()).map { currentMonth.atDay(it) }) }
    val lessonManager = LessonManager(LessonDatabase.getDatabase(context).lessonDao())

    val defaultLessons = listOf(
        LessonModel(title = "Mathématiques", description = "Algèbre et géométrie", day = "Mon", location = "Salle 101", time = "08:00", isRecurrent = true),
        LessonModel(title = "Physique", description = "Mécanique classique", day = "Tue", location = "Salle 202", time = "10:00", isRecurrent = true),
        LessonModel(title = "Chimie", description = "Chimie organique", day = "Wed", location = "Salle 305", time = "14:00", isRecurrent = true),
        LessonModel(title = "Informatique", description = "Programmation en Java", day = "Thu", location = "Salle 110", time = "16:00", isRecurrent = true),
        LessonModel(title = "Anglais", description = "Compréhension et expression", day = "Fri", location = "Salle 205", time = "09:00", isRecurrent = true),
        LessonModel(title = "Histoire", description = "Révolution française", day = "Mon", location = "Salle 302", time = "11:00", isRecurrent = true),
        LessonModel(title = "Éducation Physique", description = "Sport collectif", day = "Wed", location = "Gymnase", time = "13:00", isRecurrent = true),
        LessonModel(title = "Philosophie", description = "Cours sur Kant", day = "Fri", location = "Salle 120", time = "15:00", isRecurrent = true)
    )


    LaunchedEffect(Unit) {
        coroutineScope.launch {
            lessonManager.insertAll(defaultLessons)
            lessons = lessonManager.getAllLessons()
            events = EventManager.getEvents()
        }
    }

    val eventsDate = getEventDates(events)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary) ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            currentMonth = topWithMonth(daysInMonth)
        }
        Spacer(modifier = Modifier.size(10.dp))
        selectedDate = middleWithDays(daysInMonth, eventsDate)

        Box(){
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(10.dp)
            ) {

                val filteredLessons = trieListeLessons(lessons, selectedDate.value)
                items(filteredLessons) { lesson ->
                    Column (
                        modifier = Modifier
                        .fillMaxWidth()
                            .padding(10.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(colorResource(R.color.institutionnel_color)),
                        horizontalAlignment = Alignment.Start,
                        ){
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = lesson.time,
                                    fontSize = 15.sp,
                                    color = colorResource(R.color.black),
                                    modifier = Modifier.padding(end = 10.dp)
                                )
                                Text(
                                    text = lesson.title,
                                    fontWeight = FontWeight.SemiBold,
                                    color = colorResource(R.color.black),
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                )
                            }


                            IconButton(onClick = {
                                coroutineScope.launch {
                                    lessonManager.deleteLesson(lesson.id)
                                    lessons = lessonManager.getAllLessons()
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = context.getString(R.string.delete_lesson)
                                )
                            }
                        }
                        Row{
                            Text(
                                text = lesson.location,
                                fontSize = 15.sp,
                                color = colorResource(R.color.black),
                                modifier = Modifier
                                    .padding(8.dp)
                            )
                        }

                    }

                }

                val filteredEvents = trieListeEvents(events, selectedDate.value)
                items(filteredEvents) { event ->
                    Row(
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(15.dp))
                            .background(colorResource(R.color.pastel_purple)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween){
                        Text(
                            text = event.title,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = colorResource(R.color.black),
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                }
            }
            Row(
                modifier = Modifier.padding(10.dp).align(Alignment.BottomEnd)
            ){
                IconButton(onClick = {
                    showDialog = true
                },modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = context.getString(R.string.add_lesson)
                    )
                }
            }

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
