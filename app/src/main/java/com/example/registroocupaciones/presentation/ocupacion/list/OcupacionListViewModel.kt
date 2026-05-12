package com.example.registroocupaciones.presentation.ocupacion.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val getOcupacionesUseCase: GetOcupacionesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(OcupacionListUiState())
    val state: StateFlow<OcupacionListUiState> = _state.asStateFlow()

    init {
        loadOcupaciones()
    }

    private fun loadOcupaciones() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

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
                        errorMessage = e.message
                    )
                }
            }
        }
    }
}