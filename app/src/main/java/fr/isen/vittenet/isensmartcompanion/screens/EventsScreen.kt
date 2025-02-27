package fr.isen.vittenet.isensmartcompanion.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import fr.isen.vittenet.isensmartcompanion.EventDetailActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import fr.isen.vittenet.isensmartcompanion.R
import fr.isen.vittenet.isensmartcompanion.interfaces.EventService
import fr.isen.vittenet.isensmartcompanion.models.EventModel
import fr.isen.vittenet.isensmartcompanion.getCategoryColor


const val BASE_URL = "https://isen-smart-companion-default-rtdb.europe-west1.firebasedatabase.app/"

val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

val eventService: EventService = retrofit.create(EventService::class.java)

/*val fakeEvents = listOf(
    EventModel("1", "BDE Evening", "Soirée BDE animée par des DJs", "15/09/2023", "Campus", "BDE"),
    EventModel("2", "Gala", "Gala de fin d'année pour les étudiants", "20/10/2023", "Salle des fêtes", "BDE"),
    EventModel("3", "Cohesion Day", "Journée de cohésion et d'activités ludiques", "05/11/2023", "Parc", "BDS")
)*/

@Composable
fun EventsScreen() {
    val context = LocalContext.current
    var events by remember { mutableStateOf<List<EventModel>>(emptyList()) }
    LaunchedEffect(Unit) {
        try {
            events = eventService.getEvents()
        } catch (e: Exception) {
            Toast.makeText(context, "Error fetching events: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    Row {
        Text(text = "Evenements à venir")
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 50.dp)
            .fillMaxSize()
            .padding(horizontal = 50.dp)
    ) {
        items(events) { event ->
            val isNotified = getIsNotified(context, event.title)
            val categoryColor = getCategoryColor(event.category)
            Box(
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            val intent = Intent(context, EventDetailActivity::class.java)
                            intent.putExtra("event", event)
                            context.startActivity(intent);
                        }
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "",
                            modifier = Modifier
                                .padding(end = 20.dp)
                                .fillMaxHeight()
                                .width(15.dp)
                                .background(categoryColor)
                                .padding(vertical = 16.dp)
                        )
                        Text(
                            text = event.title,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )

                    }

                }
                Icon(
                    painter = painterResource(
                        if (isNotified) R.drawable.notif_clicked else R.drawable.notif_unclicked
                    ),
                    contentDescription = if (isNotified) "Notification active" else "Notification inactive",
                    tint = colorResource(R.color.white),
                    modifier = Modifier
                        .offset(y = (-10).dp)
                        .clip(CircleShape)
                        .background(colorResource(R.color.red))
                        .align(Alignment.TopEnd)

                )
            }
            Spacer(modifier = Modifier.size(15.dp))
        }
    }
}

@Composable
fun ColoredCircle(color: Color, size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        drawCircle(color = color)
    }
}

@Composable
fun getIsNotified(context: Context, eventTitle: String): Boolean {
    val sharedPreferences = remember {
        context.getSharedPreferences("event_prefs", Context.MODE_PRIVATE)
    }
    return sharedPreferences.getBoolean(eventTitle, false) // Retourne true si l'événement a été notifié, sinon false
}
