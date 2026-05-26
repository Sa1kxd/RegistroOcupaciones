package com.example.registroempleados.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [EmpleadoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class EmpleadoDatabase : RoomDatabase() {
    abstract fun empleadoDao(): EmpleadoDao
}
