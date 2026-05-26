package com.example.registroempleados.presentation.empleado.horasextras

import com.example.registroempleados.domain.model.Empleado
import com.example.registroempleados.domain.usecase.ReporteHorasExtras

data class HorasExtrasUiState(
    val empleados: List<Empleado> = emptyList(),
    val selectedEmpleado: Empleado? = null,
    val isDropdownExpanded: Boolean = false,
    val horasSemanalesInput: String = "",
    val horasSemanalesError: String? = null,
    val horasNocturnasInput: String = "",
    val horasNocturnasError: String? = null,
    val reporte: ReporteHorasExtras? = null
)