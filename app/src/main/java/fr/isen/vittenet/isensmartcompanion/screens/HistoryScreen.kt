package fr.isen.vittenet.isensmartcompanion.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import fr.isen.vittenet.isensmartcompanion.data.AppDatabase
import fr.isen.vittenet.isensmartcompanion.models.ChatModel
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen(innerPadding: PaddingValues, database: AppDatabase) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val db = database

    // Stocker la liste des messages récupérés depuis la base de données
    var chatMessages by remember { mutableStateOf<List<ChatModel>>(emptyList()) }

    // Charger les messages une seule fois lors de la composition
    LaunchedEffect(Unit) {
        chatMessages = db.chatDao().getAllChats()
    }

    // Affichage de la liste des questions/réponses
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Text(
            text = "Historique",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )
        IconButton(
            onClick = {
                coroutineScope.launch {
                    db.chatDao().deleteAllChats() // 🔥 Supprime tout
                    chatMessages = emptyList() // 🔄 Met à jour la liste pour refléter les changements
                }
            },
            colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.error)
        ) {
            Icon(Icons.Filled.Clear, contentDescription = "Vider l'historique")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(chatMessages) { chat ->
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Question : ${chat.question}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Réponse : ${chat.answer}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Text(
                            text = "Timestamp : ${chat.timestamp}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                db.chatDao().deleteChat(chat.id) // Supprime l'élément de la BD
                                chatMessages = db.chatDao().getAllChats() // Met à jour la liste
                            }
                        },
                        colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.Filled.Clear, contentDescription = "Supprimer")
                    }
                }
            }
        }
    }
}
