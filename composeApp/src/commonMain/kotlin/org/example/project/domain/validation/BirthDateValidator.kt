package org.example.project.domain.validation


class BirthDateValidator : Validator<String> {
    override fun validate(input: String): ValidationResult {
        // Asegura formato DD/MM/AAAA por ejemplo
        val regex = Regex("""\d{2}/\d{2}/\d{4}""")
        return if (regex.matches(input.trim())) {
            ValidationResult(true)
        } else {
            ValidationResult(false, "Fecha inválida. Use formato DD/MM/AAAA.")
        }
    }
}