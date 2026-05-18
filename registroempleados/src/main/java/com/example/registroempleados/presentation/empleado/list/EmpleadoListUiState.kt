package com.example.registroempleados.presentation.empleado.list

import com.example.registroempleados.domain.model.Empleado

data class EmpleadoListUiState(
    val isLoading: Boolean = false,
    val empleados: List<Empleado> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: Int? = null,
    val error: String? = null,
    val filtroSeleccionado: String = "Ninguno",
    val textoFiltro: String = ""
)