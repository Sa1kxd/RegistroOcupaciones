package com.example.registroempleados.domain.usecase

data class ValidationResult(
    val isValid: Boolean,
    val error: String? = null
)

fun validateNombres(nombres: String): ValidationResult {
    return when {
        nombres.isBlank() -> ValidationResult(false, "El nombre no puede estar vacío")
        nombres.length < 3 -> ValidationResult(false, "El nombre debe tener al menos 3 caracteres")
        else -> ValidationResult(true)
    }
}

fun validateFechaIngreso(fecha: String): ValidationResult {
    return when {
        fecha.isBlank() -> ValidationResult(false, "La fecha de ingreso no puede estar vacía")
        else -> ValidationResult(true)
    }
}

fun validateSexo(sexo: String): ValidationResult {
    return when {
        sexo.isBlank() -> ValidationResult(false, "Debe seleccionar el sexo del empleado")
        else -> ValidationResult(true)
    }
}

fun validateSueldo(sueldo: String): ValidationResult {
    return when {
        sueldo.isBlank() -> ValidationResult(false, "El sueldo no puede estar vacío")
        sueldo.toDoubleOrNull() == null -> ValidationResult(false, "El sueldo debe ser un número")
        sueldo.toDouble() <= 0 -> ValidationResult(false, "El sueldo debe ser mayor a 0")
        else -> ValidationResult(true)
    }
}