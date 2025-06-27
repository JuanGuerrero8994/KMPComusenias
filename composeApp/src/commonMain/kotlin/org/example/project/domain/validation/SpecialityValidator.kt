package org.example.project.domain.validation

import org.example.project.ui.theme.NOT_EMPTY

class SpecialtyValidator : Validator<String> {

    override fun validate(input: String): ValidationResult {
        return if (input.trim().isEmpty()) {
            ValidationResult(false, NOT_EMPTY)
        } else {
            ValidationResult(true)
        }
    }
}