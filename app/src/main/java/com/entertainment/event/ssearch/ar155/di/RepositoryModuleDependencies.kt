package com.entertainment.event.ssearch.ar155.di

import com.entertainment.event.ssearch.data.battery_repository.BatteryRepositoryImpl
import com.entertainment.event.ssearch.domain.battery.BatteryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModuleDependencies {

    @Binds
    fun bindBatteryRepositoryToBatteryRepositoryImpl(batteryRepositoryImpl: BatteryRepositoryImpl): BatteryRepository

}