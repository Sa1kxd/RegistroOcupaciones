package com.example.registroempleados.presentation.empleado.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.registroempleados.domain.usecase.DeleteEmpleadoUseCase
import com.example.registroempleados.domain.usecase.ObserveEmpleadosByFechaUseCase
import com.example.registroempleados.domain.usecase.ObserveEmpleadosByNombreUseCase
import com.example.registroempleados.domain.usecase.ObserveEmpleadosUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmpleadoListViewModel @Inject constructor(
    private val observeEmpleadosUseCase: ObserveEmpleadosUseCase,
    private val observeEmpleadosByNombreUseCase: ObserveEmpleadosByNombreUseCase,
    private val observeEmpleadosByFechaUseCase: ObserveEmpleadosByFechaUseCase,
    private val deleteEmpleadoUseCase: DeleteEmpleadoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EmpleadoListUiState(isLoading = true))
    val state: StateFlow<EmpleadoListUiState> = _state.asStateFlow()

    private var observeJob: Job? = null

    init {
        loadEmpleados()
    }

    fun onEvent(event: EmpleadoListUiEvent) {
        when (event) {
            EmpleadoListUiEvent.Load -> loadEmpleados()
            EmpleadoListUiEvent.Refresh -> loadEmpleados()
            is EmpleadoListUiEvent.Delete -> onDelete(event.id)
            is EmpleadoListUiEvent.ShowMessage -> _state.update { it.copy(message = event.message) }
            EmpleadoListUiEvent.ClearMessage -> _state.update { it.copy(message = null) }
            EmpleadoListUiEvent.CreateNew -> _state.update { it.copy(navigateToCreate = true) }
            is EmpleadoListUiEvent.Edit -> _state.update { it.copy(navigateToEditId = event.id) }
            is EmpleadoListUiEvent.OnFiltroChanged -> {
                _state.update { it.copy(filtroSeleccionado = event.filtro, textoFiltro = "") }
                loadEmpleados()
            }
            is EmpleadoListUiEvent.OnTextoFiltroChanged -> {
                _state.update { it.copy(textoFiltro = event.texto) }
                loadEmpleados()
            }
        }
    }

    fun loadEmpleados() {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val currentState = state.value
            val flow = when {
                currentState.textoFiltro.isBlank() -> observeEmpleadosUseCase()
                currentState.filtroSeleccionado == "Nombre" -> observeEmpleadosByNombreUseCase(currentState.textoFiltro)
                currentState.filtroSeleccionado == "Fecha" -> observeEmpleadosByFechaUseCase(currentState.textoFiltro)
                else -> observeEmpleadosUseCase()
            }

            flow.collectLatest { list ->
                _state.update { it.copy(isLoading = false, empleados = list, message = null) }
            }
        }
    }

    private fun onDelete(id: Int) {
        viewModelScope.launch {
            deleteEmpleadoUseCase(id)
            onEvent(EmpleadoListUiEvent.ShowMessage("Eliminado"))
        }
    }
}