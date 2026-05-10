package com.example.registroocupaciones.domain.usecase

data class ValidationResult(
    val isValid: Boolean,
    val error: String? = null
)

fun validateDescripcion(descripcion: String): ValidationResult {
    if (descripcion.isBlank()) {
        return ValidationResult(isValid = false, error = "La descripción es obligatoria.")
    }
    return ValidationResult(isValid = true)
}

fun validateSueldo(sueldo: Double): ValidationResult {
    if (sueldo <= 0.0) {
        return ValidationResult(isValid = false, error = "El sueldo es obligatorio y debe ser mayor a 0.")
    }
    return ValidationResult(isValid = true)
}