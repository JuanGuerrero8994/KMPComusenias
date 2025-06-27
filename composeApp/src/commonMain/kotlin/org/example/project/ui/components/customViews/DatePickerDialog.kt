// package org.example.project.ui.components.customViews // Asegúrate que el package sea correcto
package org.example.project.ui.components.customViews // Usando un nombre de paquete común

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.DropdownMenu // Importa el de Material
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.*
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties


// Helper para obtener la fecha actual con kotlinx-datetime
fun LocalDate.Companion.now(clock: Clock = Clock.System): LocalDate {
    return clock.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
}

// Función para obtener el número de días en un mes para un año específico
fun getDaysInMonth(year: Int, month: Month): Int {
    if (month == Month.FEBRUARY) {
        return if (isLeapYear(year)) 29 else 28
    }
    val monthsWith30Days = listOf(Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER)
    return if (month in monthsWith30Days) 30 else 31
}

// Helper para saber si un año es bisiesto
fun isLeapYear(year: Int): Boolean {
    return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
}

// Extensiones para facilitar la modificación de LocalDate con kotlinx-datetime
fun LocalDate.withDayOfMonth(dayOfMonth: Int): LocalDate {
    // La validación del rango se hace antes de llamar o por el propio control de UI
    return LocalDate(this.year, this.month, dayOfMonth)
}

fun LocalDate.withMonthNumber(monthNumber: Int): LocalDate {
    val newMonth = Month(monthNumber)
    val daysInNewMonth = getDaysInMonth(this.year, newMonth)
    val day = if (this.dayOfMonth > daysInNewMonth) daysInNewMonth else this.dayOfMonth
    return LocalDate(this.year, newMonth, day)
}

fun LocalDate.withYearValue(yearValue: Int): LocalDate {
    val daysInMonthForNewYear = getDaysInMonth(yearValue, this.month)
    val day = if (this.dayOfMonth > daysInMonthForNewYear) daysInMonthForNewYear else this.dayOfMonth
    return LocalDate(yearValue, this.month, day)
}



@Composable
fun DatePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    initialDate: LocalDate = LocalDate.now()
) {
    var selectedDate by remember { mutableStateOf(initialDate) }
    val today = LocalDate.now()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties()
    ) {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Selecciona una fecha")

                Spacer(Modifier.padding(4.dp))

                CustomDropdownMenu(
                    selected = selectedDate.dayOfMonth,
                    range = 1..getDaysInMonth(selectedDate.year, selectedDate.month),
                    label = "Día",
                    onSelect = { selectedDate = selectedDate.withDayOfMonth(it) }
                )

                Spacer(Modifier.padding(4.dp))

                CustomDropdownMenu(
                    selected = selectedDate.monthNumber,
                    range = 1..12,
                    label = "Mes",
                    onSelect = { selectedDate = selectedDate.withMonthNumber(it) }
                )

                Spacer(Modifier.padding(4.dp))

                CustomDropdownMenu(
                    selected = selectedDate.year,
                    range = (today.year - 100)..today.year,
                    label = "Año",
                    onSelect = { selectedDate = selectedDate.withYearValue(it) }
                )

                Spacer(Modifier.padding(8.dp))

                Row {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = {
                        onDateSelected(selectedDate)
                        onDismiss()
                    }) {
                        Text("Aceptar")
                    }
                }
            }
        }
    }
}


@Composable
fun CustomDropdownMenu(
    selected: Int,
    range: IntRange,
    label: String, // <- ya está pasando el label desde afuera
    onSelect: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selected.toString(),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) }, // <- Acá usamos el label recibido
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            range.forEach { itemValue ->
                DropdownMenuItem(onClick = {
                    onSelect(itemValue)
                    expanded = false
                }) {
                    Text(itemValue.toString())
                }
            }
        }
    }
}

