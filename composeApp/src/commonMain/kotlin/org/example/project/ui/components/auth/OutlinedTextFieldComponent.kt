package org.example.project.ui.components.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import kmpcomusenias.composeapp.generated.resources.Res
import kmpcomusenias.composeapp.generated.resources.baseline_arrow_back_24
import org.jetbrains.compose.resources.painterResource

import kmpcomusenias.composeapp.generated.resources.visibility
import kmpcomusenias.composeapp.generated.resources.visibility_off

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
                                painter = painterResource(resource = if (expanded) Res.drawable.baseline_arrow_back_24 else Res.drawable.baseline_arrow_back_24),
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
        //DatePickerFieldToModal(onDateSelected = onValueChange)
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

