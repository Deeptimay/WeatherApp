package com.example.weatherapp.di

import com.example.weatherapp.data.repository.WeatherAppRepositoryImpl
import com.example.weatherapp.domain.repositoryAbstraction.WeatherAppRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun providesWaypointRepository(impl: WeatherAppRepositoryImpl): WeatherAppRepository
}
