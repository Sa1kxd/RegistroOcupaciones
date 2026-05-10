package com.example.registroocupaciones.domain.usecase

import com.example.registroocupaciones.domain.repository.OcupacionRepository
import javax.inject.Inject

class DeleteOcupacionUseCase @Inject constructor(
    private val repository: OcupacionRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.delete(id)
    }
}