package org.example.project.domain.validation

interface Validator<T> {
    fun validate(input: T): ValidationResult
}

