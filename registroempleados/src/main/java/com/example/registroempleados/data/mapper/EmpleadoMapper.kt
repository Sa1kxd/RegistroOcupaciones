package com.example.registroempleados.data.mapper

import com.example.registroempleados.data.local.EmpleadoEntity
import com.example.registroempleados.domain.model.Empleado

fun EmpleadoEntity.toDomain() = Empleado(
    empleadoId = empleadoId,
    fechaIngreso = fechaIngreso,
    nombres = nombres,
    sexo = sexo,
    sueldo = sueldo
)

fun Empleado.toEntity()= EmpleadoEntity(
    empleadoId = empleadoId,
    fechaIngreso = fechaIngreso,
    nombres = nombres,
    sexo = sexo,
    sueldo = sueldo
)