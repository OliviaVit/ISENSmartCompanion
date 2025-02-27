package fr.isen.vittenet.isensmartcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import fr.isen.vittenet.isensmartcompanion.data.AppDatabase
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val context = LocalContext.current

            val homeTab = NavBarItem(title = ContextCompat.getString(context, R.string.home), selectedIcon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home)
            val eventsTab = NavBarItem(title = ContextCompat.getString(context, R.string.events), selectedIcon = Icons.Filled.DateRange, unselectedIcon = Icons.Outlined.DateRange, badgeAmount = 7)
            val historyTab = NavBarItem(title = ContextCompat.getString(context, R.string.history), selectedIcon = Icons.Filled.Menu, unselectedIcon = Icons.Outlined.Menu)

            val navBarItems = listOf(homeTab, eventsTab, historyTab)
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
                        }

                    }

                }
            }
        }
    }
}

