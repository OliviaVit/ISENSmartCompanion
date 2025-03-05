package fr.isen.vittenet.isensmartcompanion.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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




    LaunchedEffect(Unit) {
        coroutineScope.launch {
            lessons = lessonManager.getAllLessons()
            events = EventManager.getEvents()
        }
    }
    val eventsDate = getEventDates(events)
    println("Dates des événements : $eventsDate")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.fond)) ,
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
                    Row(
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(15.dp))
                            .background(colorResource(R.color.technologique_color)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(){
                            Text(
                                text = lesson.time,
                                fontSize = 15.sp,
                                modifier = Modifier
                                    .padding(8.dp)
                            )
                            Text(
                                text = lesson.title,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(8.dp)
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
                                contentDescription = "Supprimer un cours"
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
                            .background(colorResource(R.color.international_color)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween){
                        Text(
                            text = event.title,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
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
                    .background(colorResource(R.color.purple_200))) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Ajouter un cours"
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
