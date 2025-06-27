package org.example.project.domain.validation

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)