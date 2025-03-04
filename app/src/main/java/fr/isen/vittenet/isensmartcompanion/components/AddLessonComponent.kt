package fr.isen.vittenet.isensmartcompanion.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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

    val joursSemaine = listOf("Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche")
    val selectedDays = remember { mutableStateMapOf<String, Boolean>().apply { joursSemaine.forEach { put(it, false) } } }
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
            Column {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Titre") })
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
                OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Salle") })

                Row {
                    Button(onClick = { isRecurrent = true }, enabled = !isRecurrent) { Text("Événement récurrent") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { isRecurrent = false }, enabled = isRecurrent) { Text("Événement unique") }
                }

                if (isRecurrent) {
                    Column {
                        Text("Choisissez les jours :")
                        joursSemaine.forEach { jour ->
                            Row(
                                modifier = Modifier.toggleable(value = selectedDays[jour] == true) {
                                    selectedDays[jour] = it
                                }
                            ) {
                                Checkbox(checked = selectedDays[jour] == true, onCheckedChange = { selectedDays[jour] = it })
                                Text(jour)
                            }
                        }
                    }
                } else {
                    Button(onClick = { datePickerDialog.show() }) { Text("Sélectionner une date") }
                    selectedDate?.let { Text("Date sélectionnée : ${it.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}") }
                }

                Button(onClick = { timePickerDialog.show() }) { Text("Sélectionner une heure") }
                time?.let { Text("Heure sélectionnée : ${it}") }
            }
        },
        confirmButton = {
            Button(onClick = {
                val selectedDaysList = if (isRecurrent) selectedDays.filterValues { it }.keys.joinToString(", ") else ""
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
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) { Text("Annuler") }
        }
    )
}
