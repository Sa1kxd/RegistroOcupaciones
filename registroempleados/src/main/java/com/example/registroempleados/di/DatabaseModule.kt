package com.example.registroempleados.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.example.registroempleados.data.local.EmpleadoDao
import com.example.registroempleados.data.local.EmpleadoDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideEmpleadoDatabase(
        @ApplicationContext context: Context
    ): EmpleadoDatabase {
        return Room.databaseBuilder(
            context,
            EmpleadoDatabase::class.java,
            "empleado_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideEmpleadoDao(database: EmpleadoDatabase): EmpleadoDao {
        return database.empleadoDao()
    }
}