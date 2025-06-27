package org.example.project.domain.validation

class PhoneValidator : Validator<String> {
    override fun validate(input: String): ValidationResult {
        val phoneRegex = Regex("^\\+?[0-9]{7,15}$")
        return if (phoneRegex.matches(input.trim())) {
            ValidationResult(true)
        } else {
            ValidationResult(false, "Número de teléfono inválido.")
        }
    }
}