package org.example.project.domain.validation

class FullNameValidator : Validator<String> {
    override fun validate(input: String): ValidationResult {
        return if (input.trim().length >= 3) {
            ValidationResult(true)
        } else {
            ValidationResult(false, "El nombre debe tener al menos 3 caracteres.")
        }
    }
}