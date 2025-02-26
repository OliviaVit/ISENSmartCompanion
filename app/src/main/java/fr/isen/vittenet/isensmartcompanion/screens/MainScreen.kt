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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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

    val responses = remember { mutableStateListOf<String>() }
    val chatDao = db.chatDao()


    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(innerPadding)
            .background(colorResource(id = R.color.grey_background)),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_isen),
            context.getString(R.string.logo_isen),
            contentScale = ContentScale.Fit,
            modifier = Modifier.padding(top = 50.dp)
                .size(width = 300.dp, height = 100.dp)
        )
        Text(
            text = getString(context, R.string.app_name),
            fontSize = 25.sp,
            modifier = Modifier.padding(top = 10.dp)
        )
        Text("", modifier = Modifier
            .fillMaxSize()
            .weight(0.5F)
        )
        Box{
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                items(items = responses) { response ->
                    Text(
                        text = response,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.BottomCenter)
                    .padding(10.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(colorResource(id = R.color.grey_input)),
                verticalAlignment = Alignment.CenterVertically,

                ){
                TextField(
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
                    modifier = Modifier.weight(1F)
                )
                IconButton(onClick = {

                    coroutineScope.launch {
                        try {
                            val responseText = generateResponse(userInput.value)
                            Toast.makeText(context, "Send", Toast.LENGTH_LONG).show()
                            responses.add(responseText)
                            chatDao.insert(ChatModel(question = userInput.value, answer = responseText))
                            userInput.value = ""

                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                },
                    modifier = Modifier
                        .background(colorResource(id = R.color.red), shape = CircleShape)
                        .border(width = 5.dp, color = colorResource(id = R.color.grey_input), shape = CircleShape),

                    content = {
                        Image(
                            painterResource(R.drawable.send),
                            context.getString(R.string.send_button),
                            colorFilter = ColorFilter.tint(Color.White),
                            modifier = Modifier.size(15.dp)

                        )
                    },
                )
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