package com.example.registroempleados.presentation.empleado.form

sealed class EmpleadoFormUiEvent {
    data class Load(val id: Int?) : EmpleadoFormUiEvent()
    data class NombresChanged(val value: String) : EmpleadoFormUiEvent()
    data class FechaIngresoChanged(val value: String) : EmpleadoFormUiEvent()
    data class SexoChanged(val value: String) : EmpleadoFormUiEvent()
    data class SueldoChanged(val value: String) : EmpleadoFormUiEvent()
    object Save : EmpleadoFormUiEvent()
    object Delete : EmpleadoFormUiEvent()
}