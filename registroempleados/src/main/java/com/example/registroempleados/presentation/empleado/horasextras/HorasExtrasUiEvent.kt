package com.example.registroempleados.presentation.empleado.horasextras

import com.example.registroempleados.domain.model.Empleado

sealed class HorasExtrasUiEvent {
    data class OnEmpleadoSelected(val empleado: Empleado) : HorasExtrasUiEvent()
    data class OnDropdownExpandedChanged(val isExpanded: Boolean) : HorasExtrasUiEvent()
    data class OnHorasSemanalesChanged(val horas: String) : HorasExtrasUiEvent()
    data class OnHorasNocturnasChanged(val horas: String) : HorasExtrasUiEvent()
    object Calcular : HorasExtrasUiEvent()
    object Limpiar : HorasExtrasUiEvent()
}