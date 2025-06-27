package org.example.project.domain.validation

import org.example.project.ui.theme.INVALID_EMAIL


class EmailValidator : Validator<String> {

    private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")

    override fun validate(input: String): ValidationResult {
        return if (EMAIL_REGEX.matches(input.trim())) {
            ValidationResult(true)
        } else {
            ValidationResult(false, INVALID_EMAIL)
        }
    }
}