package com.example.registroocupaciones.domain.usecase

import com.example.registroocupaciones.domain.model.Ocupacion
import com.example.registroocupaciones.domain.repository.OcupacionRepository
import javax.inject.Inject

class GetOcupacionUseCase @Inject constructor(
    private val repository: OcupacionRepository
) {
    suspend operator fun invoke(id: Int): Ocupacion? {
        return repository.getOcupacion(id)
    }
}