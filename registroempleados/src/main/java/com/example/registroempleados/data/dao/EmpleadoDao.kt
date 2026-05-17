package com.example.registroempleados.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import com.example.registroempleados.data.local.EmpleadoEntity

@Dao
interface EmpleadoDao {
    @Upsert
    suspend fun upsert(empleado: EmpleadoEntity)

    @Query("DELETE FROM empleados WHERE empleadoId = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM empleados")
    fun getEmpleados(): Flow<List<EmpleadoEntity>>

    @Query("SELECT * FROM empleados WHERE empleadoId = :id")
    suspend fun getEmpleadoById(id: Int): EmpleadoEntity?

    @Query("SELECT * FROM empleados WHERE LOWER(nombres) = LOWER(:nombre) LIMIT 1")
    suspend fun getEmpleadoByNombreExacto(nombre: String): EmpleadoEntity?

    @Query("SELECT * FROM empleados WHERE LOWER(nombres) LIKE '%' || LOWER(:nombre) || '%'")
    fun getEmpleadosByNombre(nombre: String): Flow<List<EmpleadoEntity>>

    @Query("SELECT * FROM empleados WHERE fechaIngreso = :fecha")
    fun getEmpleadosByFecha(fecha: String): Flow<List<EmpleadoEntity>>
}