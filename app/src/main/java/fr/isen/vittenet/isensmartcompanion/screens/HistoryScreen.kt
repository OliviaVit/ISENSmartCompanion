package fr.isen.vittenet.isensmartcompanion.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.vittenet.isensmartcompanion.R
import fr.isen.vittenet.isensmartcompanion.data.AppDatabase
import fr.isen.vittenet.isensmartcompanion.models.ChatModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(innerPadding: PaddingValues, database: AppDatabase) {
    val coroutineScope = rememberCoroutineScope()
    var chatMessages by remember { mutableStateOf<List<ChatModel>>(emptyList()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        chatMessages = database.chatDao().getAllChats()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            Image(
                painterResource(R.drawable.open_folder),
                context.getString(R.string.folder),
                modifier = Modifier.size(50.dp)
            )
            Text(
                text = "Historique des chats",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )
        }

        Box{
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                items(chatMessages) { chat ->
                    val date = Date(chat.timestamp) // Convertir en objet Date
                    val dateFormate = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()) // Définir le format

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)

                    ) {
                        Box (
                        ){
                            Column(modifier = Modifier.background(colorResource(R.color.light_blue))) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier.fillMaxWidth().background(colorResource(R.color.blue))
                                ) {
                                    Spacer(modifier = Modifier.padding(start=14.dp))
                                    Image(
                                        painterResource(R.drawable.question),
                                        context.getString(R.string.folder),
                                        modifier = Modifier.size(25.dp)
                                    )
                                    Text(
                                        text = chat.question,
                                        style = TextStyle(fontSize = 20.sp, fontWeight =  FontWeight.Bold, color = colorResource(R.color.light_blue)),
                                        modifier = Modifier
                                            .padding(horizontal = 10.dp).padding(14.dp)
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier
                                        .background(colorResource(R.color.light_blue))
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = chat.answer,
                                        style = TextStyle(fontSize = 15.sp, fontWeight =  FontWeight.Medium),
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                                Row (
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End,
                                    modifier = Modifier
                                        .padding(7.dp)
                                        .fillMaxWidth()
                                        .background(colorResource(R.color.light_blue))
                                ){
                                    Text(
                                        text = dateFormate.format(date),
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                }

                            }
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        database.chatDao().deleteChat(chat.id)
                                        chatMessages = database.chatDao().getAllChats()
                                    }
                                },
                                modifier = Modifier.align(Alignment.TopEnd),
                                colors = IconButtonDefaults.iconButtonColors(contentColor = colorResource(R.color.white))
                            ) {
                                Icon(Icons.Filled.Clear, contentDescription = "Supprimer")
                            }

                        }
                    }

                }

            }
            Button(
                onClick = {
                    coroutineScope.launch {
                        database.chatDao().deleteAllChats()
                        chatMessages = emptyList()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.red)),
                modifier = Modifier
                    .padding(16.dp).align(Alignment.BottomCenter)

            ) {
                Text(
                    text = "Vider l'historique",
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Image(
                    painterResource(R.drawable.trash),
                    contentDescription = "Vider l'historique",
                    modifier = Modifier.size(18.dp)
                )
            }
        }

    }
}
