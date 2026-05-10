package com.example.registroocupaciones.domain.usecase

import com.example.registroocupaciones.domain.model.Ocupacion
import com.example.registroocupaciones.domain.repository.OcupacionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOcupacionesUseCase @Inject constructor(
    private val repository: OcupacionRepository
) {
    operator fun invoke(): Flow<List<Ocupacion>> {
        return repository.observeOcupaciones()
    }
}