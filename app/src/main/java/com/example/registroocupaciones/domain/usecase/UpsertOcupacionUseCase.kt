package com.example.registroocupaciones.domain.usecase

import com.example.registroocupaciones.domain.model.Ocupacion
import com.example.registroocupaciones.domain.repository.OcupacionRepository
import javax.inject.Inject

class UpsertOcupacionUseCase @Inject constructor(
    private val repository: OcupacionRepository
) {
    suspend operator fun invoke(ocupacion: Ocupacion): Result<Int> {
        val descripcionResult = validateDescripcion(ocupacion.descripcion)
        if (!descripcionResult.isValid) {
            return Result.failure(IllegalArgumentException(descripcionResult.error))
        }

        val sueldoResult = validateSueldo(ocupacion.sueldo)
        if (!sueldoResult.isValid) {
            return Result.failure(IllegalArgumentException(sueldoResult.error))
        }

        val ocupacionExistente = repository.getByDescripcion(ocupacion.descripcion)
        if (ocupacionExistente != null && ocupacionExistente.ocupacionId != ocupacion.ocupacionId) {
            return Result.failure(IllegalArgumentException("Ya existe una ocupación con esta descripción."))
        }

        return runCatching { repository.upsert(ocupacion) }
    }
}