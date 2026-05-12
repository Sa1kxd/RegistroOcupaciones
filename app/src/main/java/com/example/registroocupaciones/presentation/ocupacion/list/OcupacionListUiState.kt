package com.example.registroocupaciones.presentation.ocupacion.list

import com.example.registroocupaciones.domain.model.Ocupacion

data class OcupacionListUiState(
    val isLoading: Boolean = false,
    val ocupaciones: List<Ocupacion> = emptyList(),
    val errorMessage: String? = null
)