package fr.isen.vittenet.isensmartcompanion.components

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.vittenet.isensmartcompanion.R
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun middleWithDays(
    daysInMonth: MutableState<List<LocalDate>>,
    eventsDate: List<LocalDate>
): MutableState<LocalDate> {
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }

    val daysOfWeek = listOf("M", "T", "W", "T", "F", "S", "S")

    val firstDayOfMonth = daysInMonth.value.first().dayOfWeek.value
    val emptyDays = (1 until firstDayOfMonth).toList()


    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(emptyDays.size) {
                Box(modifier = Modifier
                    .aspectRatio(1f)
                )
            }

            items(daysInMonth.value.size) { index ->
                val day = daysInMonth.value[index]
                val isSelected = day == selectedDate.value
                val hasEvent = day in eventsDate
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) colorResource(R.color.institutionnel_color) else Color.Transparent,

                        )
                        .then(
                            if (hasEvent) Modifier.border(2.dp, Color.Red, CircleShape) else Modifier
                        )
                        .clickable { selectedDate.value = day },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = day.dayOfMonth.toString(), color = Color.Black)

                }
            }

        }
    }

    return selectedDate
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun topWithMonth(daysInMonth: MutableState<List<LocalDate>>): YearMonth? {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text(
            text = "Calendar",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = currentMonth.month.name,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = currentMonth.year.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium)

            }

            Row( modifier = Modifier
                .padding(20.dp)
            ){
                IconButton(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(30.dp)
                        .background(colorResource(R.color.institutionnel_color)),
                    onClick = {
                        currentMonth = currentMonth.minusMonths(1)
                        daysInMonth.value = (1..currentMonth.lengthOfMonth()).map { currentMonth.atDay(it) }
                    }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Mois précédent")
                }
                Spacer(modifier = Modifier.size(10.dp))
                IconButton(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(30.dp)
                        .background(colorResource(R.color.institutionnel_color)),
                    onClick = {
                        currentMonth = currentMonth.plusMonths(1)
                        daysInMonth.value = (1..currentMonth.lengthOfMonth()).map { currentMonth.atDay(it) }
                    }) {
                    Icon(Icons.Filled.ArrowForward, contentDescription = "Mois suivant")
                }
            }

        }
    }

    return currentMonth
}
