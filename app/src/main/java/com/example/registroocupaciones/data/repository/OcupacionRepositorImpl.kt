package com.example.registroocupaciones.data.repository

import com.example.registroocupaciones.data.local.OcupacionDao
import com.example.registroocupaciones.data.mapper.toDomain
import com.example.registroocupaciones.data.mapper.toEntity
import com.example.registroocupaciones.domain.model.Ocupacion
import com.example.registroocupaciones.domain.repository.OcupacionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OcupacionRepositoryImpl @Inject constructor(
    private val ocupacionDao: OcupacionDao
) : OcupacionRepository {
    override fun observeOcupaciones(): Flow<List<Ocupacion>> {
        return ocupacionDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getOcupacion(id: Int): Ocupacion? {
        return ocupacionDao.getById(id)?.toDomain()
    }

    override suspend fun getByDescripcion(descripcion: String): Ocupacion? {
        return ocupacionDao.getByDescripcion(descripcion)?.toDomain()
    }

    override suspend fun upsert(ocupacion: Ocupacion): Int {
        ocupacionDao.upsert(ocupacion.toEntity())
        return ocupacion.ocupacionId
    }

    override suspend fun delete(id: Int) {
        ocupacionDao.deleteById(id)
    }

    override suspend fun exists(id: Int): Boolean {
        return ocupacionDao.exists(id)
    }
}