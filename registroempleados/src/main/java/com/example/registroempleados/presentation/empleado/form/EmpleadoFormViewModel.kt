package com.example.registroempleados.presentation.empleado.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.registroempleados.domain.model.Empleado
import com.example.registroempleados.domain.usecase.DeleteEmpleadoUseCase
import com.example.registroempleados.domain.usecase.GetEmpleadoUseCase
import com.example.registroempleados.domain.usecase.UpsertEmpleadoUseCase
import com.example.registroempleados.domain.usecase.validateFechaIngreso
import com.example.registroempleados.domain.usecase.validateNombres
import com.example.registroempleados.domain.usecase.validateSexo
import com.example.registroempleados.domain.usecase.validateSueldo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmpleadoFormViewModel @Inject constructor(
    private val getEmpleadoUseCase: GetEmpleadoUseCase,
    private val upsertEmpleadoUseCase: UpsertEmpleadoUseCase,
    private val deleteEmpleadoUseCase: DeleteEmpleadoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EmpleadoFormUiState())
    val state: StateFlow<EmpleadoFormUiState> = _state.asStateFlow()

    fun onEvent(event: EmpleadoFormUiEvent) {
        when (event) {
            is EmpleadoFormUiEvent.Load -> loadEmpleado(event.id)
            is EmpleadoFormUiEvent.NombresChanged -> _state.update {
                it.copy(nombres = event.value, nombresError = null)
            }
            is EmpleadoFormUiEvent.FechaIngresoChanged -> _state.update {
                it.copy(fechaIngreso = event.value, fechaIngresoError = null)
            }
            is EmpleadoFormUiEvent.SexoChanged -> _state.update {
                it.copy(sexo = event.value, sexoError = null)
            }
            is EmpleadoFormUiEvent.SueldoChanged -> _state.update {
                it.copy(sueldo = event.value, sueldoError = null)
            }
            EmpleadoFormUiEvent.Save -> onSave()
            EmpleadoFormUiEvent.Delete -> onDelete()
        }
    }

    private fun loadEmpleado(id: Int) {
        if (id == 0) {
            _state.update {
                it.copy(
                    isNew = true,
                    empleadoId = null,
                    nombres = "",
                    fechaIngreso = "",
                    sexo = "",
                    sueldo = "",
                    nombresError = null,
                    fechaIngresoError = null,
                    sexoError = null,
                    sueldoError = null
                )
            }
            return
        }

        viewModelScope.launch {
            val empleado = getEmpleadoUseCase(id)
            if (empleado != null) {
                _state.update {
                    it.copy(
                        isNew = false,
                        empleadoId = empleado.empleadoId,
                        nombres = empleado.nombres,
                        fechaIngreso = empleado.fechaIngreso,
                        sexo = empleado.sexo,
                        sueldo = empleado.sueldo.toString(),
                        nombresError = null,
                        fechaIngresoError = null,
                        sexoError = null,
                        sueldoError = null
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        isNew = true,
                        empleadoId = null,
                        nombres = "",
                        fechaIngreso = "",
                        sexo = "",
                        sueldo = "",
                        nombresError = null,
                        fechaIngresoError = null,
                        sexoError = null,
                        sueldoError = null
                    )
                }
            }
        }
    }

    private fun onSave() {
        val nombresValidation = validateNombres(state.value.nombres)
        val fechaIngresoValidation = validateFechaIngreso(state.value.fechaIngreso)
        val sexoValidation = validateSexo(state.value.sexo)
        val sueldoValidation = validateSueldo(state.value.sueldo)

        if (!nombresValidation.isValid || !fechaIngresoValidation.isValid || !sexoValidation.isValid || !sueldoValidation.isValid) {
            _state.update {
                it.copy(
                    nombresError = nombresValidation.error,
                    fechaIngresoError = fechaIngresoValidation.error,
                    sexoError = sexoValidation.error,
                    sueldoError = sueldoValidation.error
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            val empleado = Empleado(
                empleadoId = state.value.empleadoId ?: 0,
                nombres = state.value.nombres.trim(),
                fechaIngreso = state.value.fechaIngreso.trim(),
                sexo = state.value.sexo.trim(),
                sueldo = state.value.sueldo.toDouble()
            )

            val result = upsertEmpleadoUseCase(empleado)

            result.onSuccess { newId ->
                _state.update {
                    it.copy(
                        isSaving = false,
                        saved = true,
                        empleadoId = newId,
                        isNew = false
                    )
                }
            }.onFailure {
                _state.update { it.copy(isSaving = false) }
            }
        }
    }

    private fun onDelete() {
        val id = state.value.empleadoId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            deleteEmpleadoUseCase(id)
            _state.update { it.copy(isDeleting = false, deleted = true) }
        }
    }
}