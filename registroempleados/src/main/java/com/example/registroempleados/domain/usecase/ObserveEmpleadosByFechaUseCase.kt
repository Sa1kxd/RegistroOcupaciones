package com.example.registroempleados.domain.usecase

import com.example.registroempleados.domain.model.Empleado
import com.example.registroempleados.domain.repository.EmpleadoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveEmpleadosByFechaUseCase @Inject constructor(
    private val repository: EmpleadoRepository
) {
    operator fun invoke(fecha: String): Flow<List<Empleado>> = repository.getEmpleadosByFecha(fecha)
}