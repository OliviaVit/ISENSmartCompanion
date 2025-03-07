package fr.isen.vittenet.isensmartcompanion.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun HistoryScreen(database: AppDatabase) {
    val coroutineScope = rememberCoroutineScope()
    var chatMessages by remember { mutableStateOf<List<ChatModel>>(emptyList()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        chatMessages = database.chatDao().getAllChats()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(R.drawable.open_folder),
                contentDescription = context.getString(R.string.open_folder),
                modifier = Modifier.size(50.dp)
            )
            Text(
                text = context.getString(R.string.history),
                fontWeight = FontWeight.Black,
                fontSize = 40.sp,
                letterSpacing = 2.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.padding(16.dp)
            )
        }

        Box{
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .background(MaterialTheme.colorScheme.background)

                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                items(chatMessages) { chat ->
                    val date = Date(chat.timestamp)
                    val dateFormate = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clip(RoundedCornerShape(30.dp)),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),

                        ) {
                        Box (
                        ){
                            Column(modifier = Modifier
                                .background(MaterialTheme.colorScheme.tertiary)

                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier.fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.tertiary)
                                ) {
                                    Spacer(modifier = Modifier.padding(start=14.dp))
                                    Image(
                                        painterResource(R.drawable.question),
                                        context.getString(R.string.folder),
                                        modifier = Modifier.size(25.dp)
                                    )
                                    Text(
                                        text = chat.question,
                                        style = TextStyle(fontSize = 20.sp,
                                            fontWeight =  FontWeight.Bold,
                                            color = colorResource(R.color.white)),
                                        modifier = Modifier
                                            .padding(horizontal = 10.dp).padding(14.dp)
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = chat.answer,
                                        color = colorResource(R.color.black),
                                        style = TextStyle(fontSize = 15.sp, fontWeight =  FontWeight.Medium),
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                                Row (
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End,
                                    modifier = Modifier

                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(7.dp)
                                ){
                                    Text(
                                        text = dateFormate.format(date),
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = colorResource(R.color.white),
                                        modifier = Modifier.padding(10.dp)
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
                                Icon(Icons.Filled.Clear, contentDescription = context.getString(R.string.delete))
                            }

                        }
                    }

                }

            }
            Spacer(modifier = Modifier.size(10.dp))
            Button(
                onClick = {
                    coroutineScope.launch {
                        database.chatDao().deleteAllChats()
                        chatMessages = emptyList()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .padding(16.dp).align(Alignment.BottomCenter)

            ) {
                Text(
                    text = context.getString(R.string.delete_all_history),
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painterResource(R.drawable.trash),
                    contentDescription = context.getString(R.string.delete_all_history),
                    modifier = Modifier.size(18.dp)
                )
            }
        }

    }
}
