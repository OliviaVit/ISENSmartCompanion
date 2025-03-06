package fr.isen.vittenet.isensmartcompanion.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(R.drawable.calendar),
                contentDescription = "Calendrier",
                modifier = Modifier.size(50.dp)
            )
            Text(
                text = "EVENTS",
                fontWeight = FontWeight.Black,
                fontSize = 40.sp,
                letterSpacing = 2.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.padding(16.dp)
            )
        }
        Spacer(modifier = Modifier.size(10.dp))
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp)
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
                                color = colorResource(R.color.black),
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
                            .padding(10.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.tertiary)
                            .padding(5.dp)
                            .size(20.dp)

                            .align(Alignment.CenterEnd)

                    )
                }
            }
        }
    }

}