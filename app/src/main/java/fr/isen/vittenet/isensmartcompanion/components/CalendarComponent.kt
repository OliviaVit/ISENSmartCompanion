package fr.isen.vittenet.isensmartcompanion.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import fr.isen.vittenet.isensmartcompanion.R
import fr.isen.vittenet.isensmartcompanion.models.EventModel
import java.time.LocalDate
import java.time.YearMonth
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun middleWithDays(
    daysInMonth: MutableState<List<LocalDate>>,
    eventsWithDate: MutableMap<LocalDate, List<EventModel>>
): MutableState<LocalDate> {
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(daysInMonth.value.size) { index ->
            val day = daysInMonth.value[index]
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        if (day == selectedDate.value) colorResource(R.color.institutionnel_color) else Color.Transparent
                    )
                    .clickable { selectedDate.value = day },
                contentAlignment = Alignment.Center
            ) {
                if (eventsWithDate.containsKey(day)) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(Color.Red)
                    )
                }
                Text(text = day.dayOfMonth.toString(), color = Color.Black)
            }
        }
    }

    return selectedDate
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun topWithMonth(daysInMonth: MutableState<List<LocalDate>>): YearMonth? {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = {
            currentMonth = currentMonth.minusMonths(1)
            daysInMonth.value = (1..currentMonth.lengthOfMonth()).map { currentMonth.atDay(it) }
        }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Mois précédent")
        }
        Text(currentMonth.month.name + " " + currentMonth.year)
        IconButton(onClick = {
            currentMonth = currentMonth.plusMonths(1)
            daysInMonth.value = (1..currentMonth.lengthOfMonth()).map { currentMonth.atDay(it) }
        }) {
            Icon(Icons.Filled.ArrowForward, contentDescription = "Mois suivant")
        }
    }
    return currentMonth
}
