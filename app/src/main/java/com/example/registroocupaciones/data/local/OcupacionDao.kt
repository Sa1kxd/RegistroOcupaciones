package com.example.registroocupaciones.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface OcupacionDao {
    @Query("SELECT * FROM ocupaciones ORDER BY ocupacionId DESC")
    fun observeAll(): Flow<List<OcupacionEntity>>

    @Query("SELECT * FROM ocupaciones WHERE ocupacionId = :id")
    suspend fun getById(id: Int): OcupacionEntity?

    @Query("SELECT * FROM ocupaciones WHERE descripcion = :descripcion LIMIT 1")
    suspend fun getByDescripcion(descripcion: String): OcupacionEntity?

    @Upsert
    suspend fun upsert(entity: OcupacionEntity)

    @Query("DELETE FROM ocupaciones WHERE ocupacionId = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM ocupaciones WHERE ocupacionId = :id)")
    suspend fun exists(id: Int): Boolean
}