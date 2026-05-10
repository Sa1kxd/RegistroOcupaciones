package com.example.registroocupaciones.data.mapper

import com.example.registroocupaciones.data.local.OcupacionEntity
import com.example.registroocupaciones.domain.model.Ocupacion

fun OcupacionEntity.toDomain(): Ocupacion = Ocupacion(
    ocupacionId = ocupacionId,
    descripcion = descripcion,
    sueldo = sueldo
)

fun Ocupacion.toEntity(): OcupacionEntity = OcupacionEntity(
    ocupacionId = ocupacionId,
    descripcion = descripcion,
    sueldo = sueldo
)