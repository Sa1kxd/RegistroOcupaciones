package com.example.registroempleados.domain.repository

import com.example.registroempleados.domain.model.Empleado
import kotlinx.coroutines.flow.Flow

interface EmpleadoRepository {
    fun getEmpleados(): Flow<List<Empleado>>
    fun getEmpleadosByNombre(nombre: String): Flow<List<Empleado>>
    fun getEmpleadosByFecha(fecha: String): Flow<List<Empleado>>
    suspend fun getEmpleadoById(id: Int): Empleado?
    suspend fun getEmpleadoByNombreExacto(nombre: String): Empleado?
    suspend fun upsert(empleado: Empleado)
    suspend fun delete(id: Int)

}