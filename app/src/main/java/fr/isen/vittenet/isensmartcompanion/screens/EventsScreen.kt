package fr.isen.vittenet.isensmartcompanion.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.isen.vittenet.isensmartcompanion.EventDetailActivity
import fr.isen.vittenet.isensmartcompanion.R
import fr.isen.vittenet.isensmartcompanion.components.getCategoryColor
import fr.isen.vittenet.isensmartcompanion.data.EventManager
import fr.isen.vittenet.isensmartcompanion.helpers.getIsNotified
import fr.isen.vittenet.isensmartcompanion.models.EventModel

@Composable
fun EventsScreen() {
    val context = LocalContext.current
    var events by remember { mutableStateOf<List<EventModel>>(emptyList()) }
    LaunchedEffect(Unit) {
        events = EventManager.getEvents()
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(R.drawable.calendar),
                contentDescription = "Calendrier",
                modifier = Modifier.size(50.dp)
            )
            Text(
                text = "Événements à venir",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )
        }
        Spacer(modifier = Modifier.size(10.dp))
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
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
                            .offset(y = (-15).dp)
                            .padding(10.dp)
                            .clip(CircleShape)
                            .background(colorResource(R.color.red))
                            .align(Alignment.TopEnd)

                    )
                }
                Spacer(modifier = Modifier.size(15.dp))
            }
        }
    }

}