package fr.isen.vittenet.isensmartcompanion

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import fr.isen.ines.isensmartcompanion.screens.CalendarScreen
import fr.isen.vittenet.isensmartcompanion.data.AppDatabase
import fr.isen.vittenet.isensmartcompanion.models.CalendarModel
import fr.isen.vittenet.isensmartcompanion.screens.BottomNavBar
import fr.isen.vittenet.isensmartcompanion.screens.EventsScreen
import fr.isen.vittenet.isensmartcompanion.screens.HistoryScreen
import fr.isen.vittenet.isensmartcompanion.screens.MainScreen
import fr.isen.vittenet.isensmartcompanion.ui.theme.ISENSmartCompanionTheme

data class NavBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null
)

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val context = LocalContext.current

            val homeTab = NavBarItem(title = ContextCompat.getString(context, R.string.home), selectedIcon = ImageVector.vectorResource(R.drawable.home_clicked), unselectedIcon = ImageVector.vectorResource(R.drawable.home_unclicked))
            val eventsTab = NavBarItem(title = ContextCompat.getString(context, R.string.events), selectedIcon = ImageVector.vectorResource(R.drawable.event_clicked), unselectedIcon = ImageVector.vectorResource(R.drawable.event_unclicked))
            val historyTab = NavBarItem(title = ContextCompat.getString(context, R.string.history), selectedIcon = ImageVector.vectorResource(R.drawable.history_clicked), unselectedIcon = ImageVector.vectorResource(R.drawable.history_unclicked))
            val calendarTab = NavBarItem(title = ContextCompat.getString(context, R.string.calendar), selectedIcon = ImageVector.vectorResource(R.drawable.calendar_clicked), unselectedIcon = ImageVector.vectorResource(R.drawable.calendar_unclicked))

            val navBarItems = listOf(homeTab, eventsTab, historyTab, calendarTab)
            val navController = rememberNavController()

            val db = AppDatabase.getDatabase(context)


            ISENSmartCompanionTheme {
                Scaffold(
                    bottomBar = { BottomNavBar(navBarItems, navController) },
                    modifier = Modifier.fillMaxSize(),) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        NavHost(navController = navController, startDestination = homeTab.title) {
                            composable(homeTab.title) {
                                MainScreen(innerPadding,db)
                            }
                            composable(eventsTab.title) {
                                EventsScreen()
                            }
                            composable(historyTab.title) {
                                HistoryScreen(innerPadding,db)
                            }
                            composable(calendarTab.title) {
                                //CalendarScreen()
                                CalendarModel()
                            }
                        }

                    }

                }
            }
        }
    }
}

