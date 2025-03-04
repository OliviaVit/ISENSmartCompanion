package fr.isen.vittenet.isensmartcompanion.screens

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.vittenet.isensmartcompanion.R
import fr.isen.vittenet.isensmartcompanion.helpers.NotificationHelper

@Composable
fun EventDetail(
    title: String, descriptionEvent: String, date: String, location: String, category: String, modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("event_prefs", Context.MODE_PRIVATE)

    var isNotified by remember { mutableStateOf(sharedPreferences.getBoolean(title, false)) }
    val editor: SharedPreferences.Editor = sharedPreferences.edit()


    val permissionLauncher = rememberLauncherForActivityResult(
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
                        if (!isNotified) {
                            NotificationHelper.showNotification(context, title, descriptionEvent, permissionLauncher)
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