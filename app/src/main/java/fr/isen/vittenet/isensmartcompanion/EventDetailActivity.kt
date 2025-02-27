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
import android.os.Handler
import android.os.Looper
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontStyle
import androidx.core.graphics.ColorUtils
import fr.isen.vittenet.isensmartcompanion.objects.NotificationHelper


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
fun EventDetail(
    title: String, descriptionEvent: String, date: String, location: String, category: String, modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("event_prefs", Context.MODE_PRIVATE)

    var isNotified by remember { mutableStateOf(sharedPreferences.getBoolean(title, false)) }
    val editor: SharedPreferences.Editor = sharedPreferences.edit()


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

    NotificationHelper.createNotificationChannel(context)

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(top = 50.dp)
            .padding(horizontal = 50.dp)
    ) {
        item {
            Row() {
                Spacer(modifier = Modifier.weight(1F))
                Button(
                    onClick = {
                        if (ActivityCompat.checkSelfPermission(
                                context, Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            notificationLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                            return@Button
                        }
                        if(!isNotified){
                            NotificationHelper.showNotification(context, title, descriptionEvent) {}
                            editor.putBoolean(title, true).apply()
                        }
                        isNotified = !isNotified

                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.red)),
                    ) {
                    Text(
                        text = "Notifications",
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Image(
                        painter = painterResource(
                            if (isNotified) R.drawable.on else R.drawable.off
                        ),
                        contentDescription = "Notification",
                        modifier = Modifier.size(width = 50.dp, height = 30.dp)
                    )
                }

            }

            Spacer(modifier = Modifier.size(20.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth().background(colorResource(R.color.blue))
                ) {
                    Text(
                        text = title,
                        style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold, color = colorResource(R.color.white), letterSpacing = 2.sp),
                        modifier = Modifier.padding(16.dp)
                    )
                }


                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillParentMaxWidth()) {
                    Text(
                        text = descriptionEvent,
                        style = TextStyle( fontWeight = FontWeight.SemiBold, fontSize = 18.sp, lineHeight = 25.sp),
                        modifier = Modifier.padding(18.dp).fillMaxWidth()
                    )
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ){
                        Image(
                            painterResource(R.drawable.calendar),
                            contentDescription = "Date",
                            modifier = Modifier.size(18.dp)
                        )
                        Text(text = date, modifier = Modifier.padding(10.dp))
                    }
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ){
                        Image(
                            painterResource(R.drawable.pin),
                            contentDescription = "Lieu",
                            modifier = Modifier.size(18.dp)
                        )
                        Text(text = location, modifier = Modifier.padding(10.dp))
                    }

                    val backgroundColor = getCategoryColor(category)

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top=10.dp)

                    ) {
                        Text(
                            text = category,
                            color = colorResource(R.color.blue),
                            modifier = Modifier
                                .clip(RoundedCornerShape(30.dp))
                                .background(backgroundColor)
                                .border(width = 2.dp, color = colorResource(R.color.blue), shape = RoundedCornerShape(30.dp))
                                .padding(5.dp)
                                .padding(horizontal = 20.dp)
                        )
                    }

                }
            }

        }
    }
}

@Composable
fun getCategoryColor(category: String): Color {
    return when (category) {
        "Vie associative" -> colorResource(id = R.color.vie_associative_color)
        "BDE" -> colorResource(id = R.color.bde_color)
        "BDS" -> colorResource(id = R.color.bds_color)
        "Professionnel" -> colorResource(id = R.color.professionnel_color)
        "Concours" -> colorResource(id = R.color.concours_color)
        "Institutionnel" -> colorResource(id = R.color.institutionnel_color)
        "Technologique" -> colorResource(id = R.color.technologique_color)
        "International" -> colorResource(id = R.color.international_color)
        else -> Color.Cyan
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
