package com.example.registroempleados.domain.usecase

import com.example.registroempleados.domain.model.Empleado
import com.example.registroempleados.domain.repository.EmpleadoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveEmpleadosUseCase @Inject constructor(
    private val repository: EmpleadoRepository
) {
    operator fun invoke(): Flow<List<Empleado>> = repository.getEmpleados()
}