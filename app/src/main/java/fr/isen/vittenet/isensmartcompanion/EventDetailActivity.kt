package fr.isen.vittenet.isensmartcompanion

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.isen.vittenet.isensmartcompanion.models.EventModel
import fr.isen.vittenet.isensmartcompanion.screens.ui.theme.ISENSmartCompanionTheme

class EventDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            /*val title = intent.getStringExtra("title") ?: "Titre non défini"
            val description = intent.getStringExtra("description") ?: "Description non définie"
            val date = intent.getStringExtra("date") ?: "date non définie"
            val location = intent.getStringExtra("location") ?: "location non définie"
            val category = intent.getStringExtra("category") ?: "category non définie"
            */
            val event = intent.getSerializableExtra("event") as? EventModel

            ISENSmartCompanionTheme {

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (event != null) {
                        EventDetail(title= event.title,description = event.description, date= event.date, location = event.location, category = event.category, modifier =Modifier.padding(innerPadding) )
                    }
                }
            }
        }
    }
}

@Composable
fun EventDetail(title: String, description: String,date: String, location: String,category: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 50.dp)
            .padding(horizontal = 50.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                    ) {
                        Text(text = title,modifier = Modifier.padding(10.dp))
                        Text(text = description,modifier = Modifier.padding(10.dp))
                        Text(text = date,modifier = Modifier.padding(10.dp))
                        Text(text = location,modifier = Modifier.padding(10.dp))
                        Text(text = category,modifier = Modifier.padding(10.dp))
                    }
                }
            }

        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    ISENSmartCompanionTheme {
        Greeting("Android")
    }
}