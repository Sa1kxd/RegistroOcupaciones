package com.example.registroempleados.presentation.empleado.form

data class EmpleadoFormUiState(
    val isNew: Boolean = true,
    val isSaving: Boolean = false,
    val saved: Boolean = false,
    val isDeleting: Boolean = false,
    val deleted: Boolean = false,
    val empleadoId: Int? = null,
    val nombres: String = "",
    val nombresError: String? = null,
    val fechaIngreso: String = "",
    val fechaIngresoError: String? = null,
    val sexo: String = "",
    val sexoError: String? = null,
    val sueldo: String = "",
    val sueldoError: String? = null
)