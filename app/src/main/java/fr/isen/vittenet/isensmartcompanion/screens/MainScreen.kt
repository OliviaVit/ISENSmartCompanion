package fr.isen.vittenet.isensmartcompanion.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import fr.isen.vittenet.isensmartcompanion.R

@Composable
fun MainScreen(innerPadding: PaddingValues) {
    val context = LocalContext.current
    var userInput = remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(innerPadding),
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
        Text(text = getString(context, R.string.app_name))
        Text("", modifier = Modifier
            .fillMaxSize()
            .weight(0.5F)
        )
        Row(
            modifier = Modifier
                .height(50.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(LightGray),
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
            OutlinedButton(onClick = {
                Toast.makeText(context,"user input : ${userInput.value}", Toast.LENGTH_LONG).show()
            },
                modifier = Modifier
                    .background(Color.Red, shape = CircleShape),

                content = {
                    Image(
                        painterResource(R.drawable.send),
                        context.getString(R.string.send_button),
                    )
                },

                )
        }

    }
}