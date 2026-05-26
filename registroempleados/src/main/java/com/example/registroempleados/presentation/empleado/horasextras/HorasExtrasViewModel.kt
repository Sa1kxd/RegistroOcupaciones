package com.example.registroempleados.presentation.empleado.horasextras

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registroempleados.domain.usecase.CalcularHorasExtrasUseCase
import com.example.registroempleados.domain.usecase.ObserveEmpleadosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HorasExtrasViewModel @Inject constructor(
    private val observeEmpleadosUseCase: ObserveEmpleadosUseCase,
    private val calcularHorasExtrasUseCase: CalcularHorasExtrasUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HorasExtrasUiState())
    val state: StateFlow<HorasExtrasUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            observeEmpleadosUseCase().collectLatest { lista ->
                _state.update { it.copy(empleados = lista) }
            }
        }
    }

    fun onEvent(event: HorasExtrasUiEvent) {
        when (event) {
            is HorasExtrasUiEvent.OnDropdownExpandedChanged -> _state.update { it.copy(isDropdownExpanded = event.isExpanded) }
            is HorasExtrasUiEvent.OnEmpleadoSelected -> _state.update {
                it.copy(
                    selectedEmpleado = event.empleado,
                    isDropdownExpanded = false,
                    reporte = null
                )
            }
            is HorasExtrasUiEvent.OnHorasSemanalesChanged -> _state.update { it.copy(horasSemanalesInput = event.horas, horasSemanalesError = null) }
            is HorasExtrasUiEvent.OnHorasNocturnasChanged -> _state.update { it.copy(horasNocturnasInput = event.horas, horasNocturnasError = null) }
            HorasExtrasUiEvent.Limpiar -> _state.update {
                it.copy(horasSemanalesInput = "", horasNocturnasInput = "", reporte = null, selectedEmpleado = null)
            }
            HorasExtrasUiEvent.Calcular -> calcular()
        }
    }

    private fun calcular() {
        val empleado = state.value.selectedEmpleado
        val horasSemana = state.value.horasSemanalesInput.toIntOrNull()
        val horasNoche = state.value.horasNocturnasInput.toIntOrNull() ?: 0

        if (empleado == null) return

        if (horasSemana == null || horasSemana <= 0) {
            _state.update { it.copy(horasSemanalesError = "Ingrese una cantidad válida") }
            return
        }

        if (horasNoche > horasSemana) {
            _state.update { it.copy(horasNocturnasError = "No pueden ser mayores a las totales") }
            return
        }

        val reporte = calcularHorasExtrasUseCase(
            sueldoMensual = empleado.sueldo,
            horasTrabajadasSemanales = horasSemana,
            horasNocturnas = horasNoche
        )

        _state.update { it.copy(reporte = reporte) }
    }
}