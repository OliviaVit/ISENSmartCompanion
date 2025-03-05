package fr.isen.vittenet.isensmartcompanion.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
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
        title = { Text("Ajouter un cours") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Titre") },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Blue,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color.Black
                    ),
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Blue,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color.Black
                    ),
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Salle") },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Blue,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color.Black
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
                            containerColor = if (!isRecurrent) Color.Blue else Color.LightGray, // ✅ Couleur normale
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.DarkGray
                        )
                    ) {
                        Text("Récurrent")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { isRecurrent = false },
                        enabled = isRecurrent,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isRecurrent) Color.Green else Color.LightGray, // ✅ Couleur normale
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray, // ✅ Couleur lorsqu'il est désactivé
                            disabledContentColor = Color.DarkGray
                        )
                    ) {
                        Text("Unique")
                    }
                }

                if (isRecurrent) {
                    Column {
                        Text("Choisissez les jours :")
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
                    Button(onClick = { datePickerDialog.show() }) { Text("Sélectionner une date") }
                    selectedDate?.let {
                        Text(
                            "Date sélectionnée : ${
                                it.format(
                                    DateTimeFormatter.ofPattern(
                                        "dd/MM/yyyy"
                                    )
                                )
                            }"
                        )
                    }
                }

                Button(onClick = { timePickerDialog.show() }) { Text("Sélectionner une heure") }
                time?.let { Text("Heure sélectionnée : ${it}") }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(), // Prend toute la largeur pour le centrage
                horizontalArrangement = Arrangement.Center // ✅ Centre les boutons
            ) {
                Button(onClick = { onDismiss() }) {
                    Text("Annuler")
                }
                Spacer(modifier = Modifier.width(16.dp)) // ✅ Espacement entre les boutons
                Button(onClick = {
                    val selectedDaysList =
                        if (isRecurrent) selectedDays.filterValues { it }.keys.joinToString(", ") else ""
                    val lesson = LessonModel(
                        id = (1..1000).random(),
                        title = title.text,
                        description = description.text,
                        day = if (isRecurrent) selectedDaysList else selectedDate?.toString() ?: "",
                        location = location.text,
                        time = time?.toString() ?: "00:00",
                        isRecurrent = isRecurrent
                    )
                    onAddLesson(lesson)
                }) {
                    Text("Ajouter")
                }
            }
        }

    )
}