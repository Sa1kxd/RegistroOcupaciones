package com.example.registroocupaciones.di

import com.example.registroocupaciones.data.repository.OcupacionRepositoryImpl
import com.example.registroocupaciones.domain.repository.OcupacionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindOcupacionRepository(
        impl: OcupacionRepositoryImpl
    ): OcupacionRepository
}