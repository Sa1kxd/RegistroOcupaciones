package com.example.registroocupaciones.presentation.ocupacion.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registroocupaciones.domain.usecase.DeleteOcupacionUseCase
import com.example.registroocupaciones.domain.usecase.GetOcupacionesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OcupacionListViewModel @Inject constructor(
    private val getOcupacionesUseCase: GetOcupacionesUseCase,
    private val deleteOcupacionUseCase: DeleteOcupacionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(OcupacionListUiState())
    val state: StateFlow<OcupacionListUiState> = _state.asStateFlow()

    init {

        onEvent(OcupacionListUiEvent.Load)
    }
    fun onEvent(event: OcupacionListUiEvent) {
        when (event) {
            OcupacionListUiEvent.Load, OcupacionListUiEvent.Refresh -> {
                loadOcupaciones()
            }
            is OcupacionListUiEvent.Delete -> {
                viewModelScope.launch {
                    deleteOcupacionUseCase(event.id)
                }
            }
            is OcupacionListUiEvent.ShowMessage -> {
                _state.update { it.copy(message = event.message) }
            }
            OcupacionListUiEvent.ClearMessage -> {
                _state.update { it.copy(message = null) }
            }
            OcupacionListUiEvent.CreateNew -> {
                _state.update { it.copy(navigateToCreate = true) }
            }
            is OcupacionListUiEvent.Edit -> {
                _state.update { it.copy(navigateToEditId = event.id) }
            }
        }
    }

    private fun loadOcupaciones() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, message = null) }

            try {
                getOcupacionesUseCase().collect { ocupaciones ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            ocupaciones = ocupaciones
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        message = e.message
                    )
                }
            }
        }
    }
}

