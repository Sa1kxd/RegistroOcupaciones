package com.example.registroempleados.domain.usecase

import com.example.registroempleados.domain.model.Empleado
import com.example.registroempleados.domain.repository.EmpleadoRepository
import javax.inject.Inject

class UpsertEmpleadoUseCase @Inject constructor(
    private val repository: EmpleadoRepository
) {
    suspend operator fun invoke(empleado: Empleado): Result<Int> {
        val nombresResult = validateNombres(empleado.nombres)
        if (!nombresResult.isValid) {
            return Result.failure(IllegalArgumentException(nombresResult.error))
        }

        val fechaIngresoResult = validateFechaIngreso(empleado.fechaIngreso)
        if (!fechaIngresoResult.isValid) {
            return Result.failure(IllegalArgumentException(fechaIngresoResult.error))
        }

        val sexoResult = validateSexo(empleado.sexo)
        if (!sexoResult.isValid) {
            return Result.failure(IllegalArgumentException(sexoResult.error))
        }

        val sueldoResult = validateSueldo(empleado.sueldo.toString())
        if (!sueldoResult.isValid) {
            return Result.failure(IllegalArgumentException(sueldoResult.error))
        }

        return runCatching {
            repository.upsert(empleado)
            empleado.empleadoId // Retornamos el ID simulando el comportamiento del maestro
        }
    }
}