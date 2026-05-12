package com.example.registroocupaciones.presentation.ocupacion.edit

sealed interface OcupacionUiEvent {
    data class Load(val id: Int?) : OcupacionUiEvent
    data class DescripcionChanged(val value: String) : OcupacionUiEvent
    data class SueldoChanged(val value: String) : OcupacionUiEvent
    data object Save : OcupacionUiEvent
    data object Delete : OcupacionUiEvent
}