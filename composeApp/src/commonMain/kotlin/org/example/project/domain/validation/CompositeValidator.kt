package org.example.project.domain.validation

class CompositeValidator<T>(
    private val validators: List<Validator<T>>
) : Validator<T> {
    override fun validate(input: T): ValidationResult {
        validators.forEach { validator ->
            val result = validator.validate(input)
            if (!result.isValid) return result
        }
        return ValidationResult(true)
    }
}