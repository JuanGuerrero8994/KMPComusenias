package org.example.project.domain.validation

import org.example.project.ui.theme.NOT_EMPTY

class PasswordValidator : Validator<String> {

    override fun validate(input: String): ValidationResult {
        if (input.length < 6) {
            return ValidationResult(false, "La contraseña debe tener al menos 6 caracteres.")
        }

        val hasUpperCase = input.any { it.isUpperCase() }
      //  val hasSpecialChar = input.any { !it.isLetterOrDigit() }

        if (!hasUpperCase) {
            return ValidationResult(false, "Debe contener al menos una letra mayúscula.")
        }

//        if (!hasSpecialChar) {
//            return ValidationResult(false, "Debe contener al menos un carácter especial.")
//        }

        return ValidationResult(true)
    }
}