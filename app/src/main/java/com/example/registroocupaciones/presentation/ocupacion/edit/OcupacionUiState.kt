package com.example.registroocupaciones.presentation.ocupacion.edit

data class OcupacionUiState(
    val ocupacionId: Int? = null,
    val descripcion: String = "",
    val descripcionError: String? = null,
    val sueldo: String = "",
    val sueldoError: String? = null,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val saved: Boolean = false,
    val deleted: Boolean = false,
    val isNew: Boolean = true,
    val errorMessage: String? = null
)