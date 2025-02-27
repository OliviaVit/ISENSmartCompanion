package fr.isen.vittenet.isensmartcompanion

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import androidx.core.content.ContextCompat.getSystemService
import fr.isen.vittenet.isensmartcompanion.models.EventModel
import fr.isen.vittenet.isensmartcompanion.screens.ui.theme.ISENSmartCompanionTheme
import kotlinx.coroutines.launch
import android.Manifest
import android.app.PendingIntent


class EventDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val event = intent.getSerializableExtra("event") as? EventModel

            ISENSmartCompanionTheme {

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (event != null) {
                        EventDetail(title= event.title, descriptionEvent = event.description, date= event.date, location = event.location, category = event.category, modifier =Modifier.padding(innerPadding) )
                    }
                }
            }
        }
    }
}

@Composable
fun EventDetail(title: String, descriptionEvent: String,date: String, location: String,category: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val notificationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                Toast.makeText(context, "Permission accordée !", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Permission refusée.", Toast.LENGTH_SHORT).show()
            }
        }
    )


    val builder = NotificationCompat.Builder(context, "1")
        .setSmallIcon(R.drawable.question)
        .setDefaults(NotificationCompat.DEFAULT_ALL)
        .setContentTitle(title)
        .setContentText(descriptionEvent)
        .setPriority(NotificationCompat.PRIORITY_HIGH)


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "channel_name"
        val descriptionText = "channel description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("1", name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(top = 50.dp)
            .padding(horizontal = 50.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier
                    .padding(16.dp)
                ) {
                    Text(text = title,modifier = Modifier.padding(10.dp))
                    Text(text = descriptionEvent,modifier = Modifier.padding(10.dp))
                    Text(text = date,modifier = Modifier.padding(10.dp))
                    Text(text = location,modifier = Modifier.padding(10.dp))
                    Text(text = category,modifier = Modifier.padding(10.dp))

                }

            }
            Button(
                onClick = {
                    with(NotificationManagerCompat.from(context)) {
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            return@with
                        }
                        notify(1, builder.build())
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.red)),
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "Etre informé",
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Image(
                    painterResource(R.drawable.trash),
                    contentDescription = "Notification",
                    modifier = Modifier.size(18.dp)
                )
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
