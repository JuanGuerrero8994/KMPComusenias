package org.example.project.domain.validation

import org.example.project.ui.theme.PASSWORD_DO_NOT_MATCH

class ConfirmPasswordValidator(private val originalPassword: String) : Validator<String> {
    override fun validate(input: String): ValidationResult {
        return if (input == originalPassword) {
            ValidationResult(true)
        } else {
            ValidationResult(false, PASSWORD_DO_NOT_MATCH)
        }
    }
}