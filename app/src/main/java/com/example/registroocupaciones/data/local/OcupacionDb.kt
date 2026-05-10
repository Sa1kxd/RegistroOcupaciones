package com.example.registroocupaciones.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [OcupacionEntity::class],
    version = 1,
    exportSchema = true
)
abstract class OcupacionDb : RoomDatabase() {
    abstract fun ocupacionDao(): OcupacionDao
}