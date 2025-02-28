package fr.isen.vittenet.isensmartcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fr.isen.vittenet.isensmartcompanion.models.EventModel
import fr.isen.vittenet.isensmartcompanion.screens.ui.theme.ISENSmartCompanionTheme
import fr.isen.vittenet.isensmartcompanion.screens.EventDetail


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
