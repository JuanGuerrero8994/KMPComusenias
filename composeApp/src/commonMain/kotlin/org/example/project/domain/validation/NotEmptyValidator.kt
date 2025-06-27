package org.example.project.domain.validation

import org.example.project.ui.theme.NOT_EMPTY


class NotEmptyValidator(private val fieldName: String = "Campo") : Validator<String> {
    override fun validate(input: String): ValidationResult {
        return if (input.trim().isEmpty()) {
            ValidationResult(
                isValid = false,
                errorMessage = NOT_EMPTY
            )
        } else {
            ValidationResult(isValid = true)
        }
    }
}