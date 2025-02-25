package fr.isen.vittenet.isensmartcompanion.screens

import android.content.Intent
import android.os.Parcelable
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.isen.vittenet.isensmartcompanion.EventDetailActivity
import fr.isen.vittenet.isensmartcompanion.MainActivity
import fr.isen.vittenet.isensmartcompanion.R

data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val category: String
)

val fakeEvents = listOf(
    Event(1, "BDE Evening", "Soirée BDE animée par des DJs", "15/09/2023", "Campus", "BDE"),
    Event(2, "Gala", "Gala de fin d'année pour les étudiants", "20/10/2023", "Salle des fêtes", "BDE"),
    Event(3, "Cohesion Day", "Journée de cohésion et d'activités ludiques", "05/11/2023", "Parc", "BDS")
)

@Composable
fun EventsScreen() {
    val context = LocalContext.current
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp)
            .padding(horizontal = 50.dp)
    ) {
        items(fakeEvents) { event ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        val intent = Intent(context, EventDetailActivity::class.java)
                        intent.putExtra("title", event.title);
                        intent.putExtra("description", event.description);
                        intent.putExtra("date", event.date);
                        intent.putExtra("location", event.location);
                        intent.putExtra("category", event.category);
                        context.startActivity(intent);
                    }
            ) {
                Text(
                    text = event.title,
                    modifier = Modifier.padding(16.dp)
                )

            }
        }
    }
}

