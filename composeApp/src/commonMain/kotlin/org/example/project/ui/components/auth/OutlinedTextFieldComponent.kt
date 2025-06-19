package org.example.project.ui.components.auth

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import kmpcomusenias.composeapp.generated.resources.Res
import kmpcomusenias.composeapp.generated.resources.baseline_arrow_back_24
import org.jetbrains.compose.resources.painterResource
import kmpcomusenias.composeapp.generated.resources.visibility
import kmpcomusenias.composeapp.generated.resources.visibility_off
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OutlinedTextFieldComponent(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    isPhone: Boolean = false,
    isBirth: Boolean = false,
    isDropdown: Boolean = false,
    dropdownOptions: List<String> = emptyList(),
    onDropdownItemSelected: ((String) -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    var passwordVisibility by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    if (isDropdown) {
        var selectedOption by remember { mutableStateOf(value) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = {},
                label = { Text(label) },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            painter = painterResource(resource = Res.drawable.baseline_arrow_back_24),
                            contentDescription = "Dropdown arrow"
                        )
                    }
                }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                dropdownOptions.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            selectedOption = option
                            onDropdownItemSelected?.invoke(option)
                            expanded = false
                        }
                    ) {
                        Text(option)
                    }
                }
            }
        }
    } else if (isBirth) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures { showDatePicker = true }
                }
        )

        if (showDatePicker) {
            DatePickerDialog(
                initialDate = value.toLocalDateOrNull() ?: currentLocalDate(),
                onDateSelected = {
                    val formatted = it.toFormattedString()
                    onValueChange(formatted)
                    showDatePicker = false
                },
                onDismissRequest = { showDatePicker = false }
            )
        }
    } else {
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            label = { Text(label) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (isPassword && !passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = trailingIcon ?: if (isPassword) {
                {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            painter = painterResource(
                                resource = if (passwordVisibility) Res.drawable.visibility else Res.drawable.visibility_off
                            ),
                            contentDescription = if (passwordVisibility) "Hide password" else "Show password"
                        )
                    }
                }
            } else null,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = when {
                    isPhone -> KeyboardType.Phone
                    isPassword -> KeyboardType.Password
                    else -> keyboardType
                }
            )
        )
    }
}


@Composable
fun DatePickerDialog(
    initialDate: LocalDate = currentLocalDate(),
    onDateSelected: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit
) {
    val selectedDate = remember { mutableStateOf(initialDate) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(onClick = { onDateSelected(selectedDate.value) }) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Cancelar")
            }
        },
        title = { Text("Selecciona una fecha") },
        text = {
            Column {
                Text("Fecha: ${selectedDate.value}") // o aplicar un formateo personalizado
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(onClick = {
                        selectedDate.value = selectedDate.value.minus(DatePeriod(days = 1))
                    }) {
                        Text("-1 día")
                    }
                    Button(onClick = {
                        selectedDate.value = selectedDate.value.plus(DatePeriod(days = 1))
                    }) {
                        Text("+1 día")
                    }
                }
            }
        }
    )
}

fun currentLocalDate(): LocalDate = Clock.System.now()
    .toLocalDateTime(TimeZone.currentSystemDefault()).date


fun LocalDate.toFormattedString(): String =
    "${dayOfMonth.toString().padStart(2, '0')}/${monthNumber.toString().padStart(2, '0')}/$year"

fun String.toLocalDateOrNull(): LocalDate? {
    return try {
        val parts = this.split("/")
        if (parts.size != 3) return null
        val day = parts[0].toInt()
        val month = parts[1].toInt()
        val year = parts[2].toInt()
        LocalDate(year, month, day)
    } catch (e: Exception) {
        null
    }
}