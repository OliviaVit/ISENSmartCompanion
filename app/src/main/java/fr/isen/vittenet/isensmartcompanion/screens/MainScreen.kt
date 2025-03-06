package fr.isen.vittenet.isensmartcompanion.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import androidx.room.Room
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.asTextOrNull
import fr.isen.vittenet.isensmartcompanion.R
import fr.isen.vittenet.isensmartcompanion.data.AppDatabase
import fr.isen.vittenet.isensmartcompanion.models.ChatModel
import kotlinx.coroutines.*

@Composable
fun MainScreen(innerPadding: PaddingValues, db: AppDatabase) {
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    var userInput = remember { mutableStateOf<String>("") }
    val paires = remember { mutableStateListOf<ChatModel>() }
    val chatDao = db.chatDao()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_isen),
                contentDescription = context.getString(R.string.logo_isen),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(width = 300.dp, height = 100.dp)
            )
        }
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(MaterialTheme.colorScheme.background)
        ) {

            Text(
                text = getString(context, R.string.app_name),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 10.dp).padding(16.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .weight(1F)
            ) {
                items(items = paires) { paire ->

                    Row(

                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()

                    ){
                        Text(
                            text = paire.question,
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(
                                        bottomStart = 18.dp,
                                        bottomEnd = 18.dp,
                                        topStart = 18.dp
                                    )
                                )
                                .background(colorResource(R.color.pastel_purple))
                                .padding(10.dp)

                        )
                    }
                    Spacer(modifier= Modifier.size(10.dp))
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()

                    ){
                        Text(
                            text = paire.answer,
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(
                                        bottomStart = 18.dp,
                                        bottomEnd = 18.dp,
                                        topEnd = 18.dp
                                    )
                                )
                                .background(colorResource(R.color.pastel_green))
                                .padding(10.dp)

                        )
                    }
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                }
            }

            Row (modifier = Modifier.fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
            ){
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.background),
                        verticalAlignment = Alignment.CenterVertically,

                        ){
                        TextField(
                            modifier = Modifier.weight(1F),

                            value = userInput.value,
                            onValueChange = { newValue ->
                                userInput.value = newValue
                            },

                            shape = RoundedCornerShape(20.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            textStyle = TextStyle(color = MaterialTheme.colorScheme.tertiary)
                        )
                        IconButton(onClick = {

                            coroutineScope.launch {
                                try {
                                    val responseText = generateResponse(userInput.value)
                                    paires.add(ChatModel(question = userInput.value, answer = responseText))
                                    chatDao.insert(ChatModel(question = userInput.value, answer = responseText))
                                    userInput.value = ""

                                } catch (e: Exception) {
                                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                            }

                        },
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.tertiary, shape = CircleShape)
                                .size(35.dp),

                            content = {
                                Image(
                                    ImageVector.vectorResource(R.drawable.send),
                                    context.getString(R.string.send_button),
                                    colorFilter = ColorFilter.tint(Color.White),
                                    modifier = Modifier.size(25.dp)

                                )
                            },
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                    }
                }
            }



        }



    }
}


suspend fun generateResponse(input: String): String {

    val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-pro-latest",
        apiKey = "AIzaSyAY3Vesc4xrW4WKU388g9KGkCxy05s-cps"
    )

    val response = generativeModel.generateContent(input)

    return response.candidates.first().content.parts.map { it.asTextOrNull() }.joinToString("\n")

}