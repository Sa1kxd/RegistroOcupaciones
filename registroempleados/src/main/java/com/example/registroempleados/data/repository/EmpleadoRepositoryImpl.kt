package com.example.registroempleados.data.repository

import com.example.registroempleados.data.dao.EmpleadoDao
import com.example.registroempleados.domain.model.Empleado
import com.example.registroempleados.domain.repository.EmpleadoRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import toDomain
import toEntity

class EmpleadoRepositoryImpl @Inject constructor(
    private val empleadoDao: EmpleadoDao
) : EmpleadoRepository {

    override fun getEmpleados(): Flow<List<Empleado>> {
        return empleadoDao.getEmpleados().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getEmpleadosByNombre(nombre: String): Flow<List<Empleado>> {
        return empleadoDao.getEmpleadosByNombre(nombre).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getEmpleadosByFecha(fecha: String): Flow<List<Empleado>> {
        return empleadoDao.getEmpleadosByFecha(fecha).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getEmpleadoById(id: Int): Empleado? {
        return empleadoDao.getEmpleadoById(id)?.toDomain()
    }

    override suspend fun getEmpleadoByNombreExacto(nombre: String): Empleado? {
        return empleadoDao.getEmpleadoByNombreExacto(nombre)?.toDomain()
    }

    override suspend fun upsert(empleado: Empleado) {
        empleadoDao.upsert(empleado.toEntity())
    }

    override suspend fun delete(id: Int) {
        empleadoDao.delete(id)
    }
}