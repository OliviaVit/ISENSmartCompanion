package fr.isen.vittenet.isensmartcompanion.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import fr.isen.vittenet.isensmartcompanion.R
import fr.isen.vittenet.isensmartcompanion.models.LessonModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddLessonDialog(onDismiss: () -> Unit, onAddLesson: (LessonModel) -> Unit) {

    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var location by remember { mutableStateOf(TextFieldValue("")) }
    var time by remember { mutableStateOf<LocalTime?>(null) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var isRecurrent by remember { mutableStateOf(true) }

    val joursSemaine = listOf("Mon", "Tue", "Wes", "Thu", "Fri", "Sat", "Sun")
    val selectedDays = remember {
        mutableStateMapOf<String, Boolean>().apply {
            joursSemaine.forEach {
                put(
                    it,
                    false
                )
            }
        }
    }
    val context = LocalContext.current

    val timePickerDialog = remember {
        TimePickerDialog(context, { _, hour, minute ->
            time = LocalTime.of(hour, minute)
        }, 12, 0, true)
    }
    val datePickerDialog = remember {
        DatePickerDialog(context, { _, year, month, dayOfMonth ->
            selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
        }, LocalDate.now().year, LocalDate.now().monthValue - 1, LocalDate.now().dayOfMonth)
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {context.getString(R.string.add_lesson)},
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = {
                        Text(text = context.getString(R.string.title),
                            color = if (isSystemInDarkTheme()) colorResource(R.color.white) else colorResource(R.color.black))
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    ),
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = {
                        Text(text = context.getString(R.string.description),
                            color = if (isSystemInDarkTheme()) colorResource(R.color.white) else colorResource(R.color.black))
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    ),
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = {
                        Text(text = context.getString(R.string.room),
                            color = if (isSystemInDarkTheme()) colorResource(R.color.white) else colorResource(R.color.black))
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    ),
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { isRecurrent = true },
                        enabled = !isRecurrent,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!isRecurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary,
                            )
                    ) {
                        Text(context.getString(R.string.recurrent))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { isRecurrent = false },
                        enabled = isRecurrent,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isRecurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary,
                            )
                    ) {
                        Text(context.getString(R.string.unique))
                    }
                }

                if (isRecurrent) {
                    Column {
                        Text( context.getString(R.string.choose_days))
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            joursSemaine.forEach { jour ->
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Checkbox(
                                        checked = selectedDays[jour] == true,
                                        onCheckedChange = { selectedDays[jour] = it }
                                    )
                                    Text(jour)
                                }
                            }
                        }
                    }
                } else {
                    Button(onClick = { datePickerDialog.show() }) { Text(context.getString(R.string.choose_date)) }
                    selectedDate?.let {
                        Text(
                            context.getString(R.string.selected_date)+" ${
                                it.format(
                                    DateTimeFormatter.ofPattern(
                                        "dd/MM/yyyy"
                                    )
                                )
                            }"
                        )
                    }
                }

                Button(onClick = { timePickerDialog.show() }) { Text(context.getString(R.string.choose_time)) }
                time?.let { Text(context.getString(R.string.selected_time)+" ${it}") }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = { onDismiss() }) {
                    Text(context.getString(R.string.cancel))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        val selectedDaysList =
                            if (isRecurrent) selectedDays.filterValues { it }.keys.joinToString(", ") else ""
                        val lesson = LessonModel(
                            id = (1..1000).random(),
                            title = title.text,
                            description = description.text,
                            day = if (isRecurrent) selectedDaysList else selectedDate?.toString()
                                ?: "",
                            location = location.text,
                            time = time?.toString() ?: "00:00",
                            isRecurrent = isRecurrent
                        )
                        onAddLesson(lesson)
                    },
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.background,
                        disabledContainerColor =MaterialTheme.colorScheme.secondary,
                        disabledContentColor = MaterialTheme.colorScheme.background ),
                ){
                    Text(context.getString(R.string.add))
                }
            }
        }

    )
}