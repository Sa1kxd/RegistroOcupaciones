package com.example.registroocupaciones.presentation.ocupacion.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.registroocupaciones.domain.model.Ocupacion
import com.example.registroocupaciones.domain.usecase.DeleteOcupacionUseCase
import com.example.registroocupaciones.domain.usecase.GetOcupacionUseCase
import com.example.registroocupaciones.domain.usecase.UpsertOcupacionUseCase
import com.example.registroocupaciones.domain.usecase.validateDescripcion
import com.example.registroocupaciones.domain.usecase.validateSueldo
import com.example.registroocupaciones.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OcupacionViewModel @Inject constructor(
    private val getOcupacionUseCase: GetOcupacionUseCase,
    private val upsertOcupacionUseCase: UpsertOcupacionUseCase,
    private val deleteOcupacionUseCase: DeleteOcupacionUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val routeArgs = savedStateHandle.toRoute<Screen.OcupacionForm>()
    private val ocupacionId: Int = routeArgs.ocupacionId

    private val _state = MutableStateFlow(OcupacionUiState())
    val state: StateFlow<OcupacionUiState> = _state.asStateFlow()

    init {
        loadOcupacion(ocupacionId)
    }

    fun onEvent(event: OcupacionUiEvent) {
        when (event) {
            is OcupacionUiEvent.Load -> loadOcupacion(event.id)
            is OcupacionUiEvent.DescripcionChanged -> _state.update {
                it.copy(descripcion = event.value, descripcionError = null, errorMessage = null)
            }
            is OcupacionUiEvent.SueldoChanged -> _state.update {
                it.copy(sueldo = event.value, sueldoError = null, errorMessage = null)
            }
            OcupacionUiEvent.Save -> onSave()
            OcupacionUiEvent.Delete -> onDelete()
        }
    }

    private fun loadOcupacion(id: Int?) {
        if (id == null || id == 0) {
            _state.update { it.copy(isNew = true, ocupacionId = null) }
            return
        }

        viewModelScope.launch {
            val ocupacion = getOcupacionUseCase(id)
            if (ocupacion != null) {
                _state.update {
                    it.copy(
                        isNew = false,
                        ocupacionId = ocupacion.ocupacionId,
                        descripcion = ocupacion.descripcion,
                        sueldo = ocupacion.sueldo.toString()
                    )
                }
            } else {
                _state.update { it.copy(isNew = true, ocupacionId = null) }
            }
        }
    }

    private fun onSave() {
        val descripcion = state.value.descripcion
        val sueldoDouble = state.value.sueldo.toDoubleOrNull() ?: 0.0

        val descripcionValidation = validateDescripcion(descripcion)
        val sueldoValidation = validateSueldo(sueldoDouble)

        if (!descripcionValidation.isValid || !sueldoValidation.isValid) {
            _state.update {
                it.copy(
                    descripcionError = descripcionValidation.error,
                    sueldoError = sueldoValidation.error
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, errorMessage = null) }

            val ocupacion = Ocupacion(
                ocupacionId = state.value.ocupacionId ?: 0,
                descripcion = descripcion,
                sueldo = sueldoDouble
            )

            val result = upsertOcupacionUseCase(ocupacion)
            result.onSuccess { newId ->
                _state.update {
                    it.copy(
                        isSaving = false,
                        saved = true,
                        ocupacionId = newId,
                        isNew = false
                    )
                }
            }.onFailure { exception ->
                // Si la validación de duplicados falla en el UseCase, se atrapa aquí
                _state.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = exception.message
                    )
                }
            }
        }
    }

    private fun onDelete() {
        val id = state.value.ocupacionId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            deleteOcupacionUseCase(id)
            _state.update { it.copy(isDeleting = false, deleted = true) }
        }
    }
}