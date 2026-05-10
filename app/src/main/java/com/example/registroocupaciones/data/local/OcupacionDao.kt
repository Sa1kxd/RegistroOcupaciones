package com.example.registroocupaciones.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface OcupacionDao {
    @Upsert
    suspend fun save(ocupacion: OcupacionEntity)

    @Delete
    suspend fun delete(ocupacion: OcupacionEntity)

    @Query("SELECT * FROM ocupaciones")
    fun getAll(): Flow<List<OcupacionEntity>>

    @Query("SELECT * FROM ocupaciones WHERE descripcion = :descripcion LIMIT 1")
    suspend fun getByDescripcion(descripcion: String): OcupacionEntity?
}