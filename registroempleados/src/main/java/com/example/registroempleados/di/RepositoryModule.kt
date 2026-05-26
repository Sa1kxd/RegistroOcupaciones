package com.example.registroempleados.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.example.registroempleados.data.repository.EmpleadoRepositoryImpl
import com.example.registroempleados.domain.repository.EmpleadoRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindEmpleadoRepository(
        impl: EmpleadoRepositoryImpl
    ): EmpleadoRepository
}