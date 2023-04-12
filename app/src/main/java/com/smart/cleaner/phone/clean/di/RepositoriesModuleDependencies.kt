package com.smart.cleaner.phone.clean.di

import com.softcleean.fastcleaner.data.battery_provider.BatteryRepositoryImpl
import com.softcleean.fastcleaner.data.boost_provider.BoostRepositoryImpl
import com.softcleean.fastcleaner.domain.battery.BatteryRepository
import com.softcleean.fastcleaner.domain.boost.BoostRepository
import com.softcleean.fastcleaner.domain.boost.BoostUseCase
import com.softcleean.fastcleaner.domain.boost.BoostUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoriesModuleDependencies {

    @Binds
    fun bindBatteryRepositoryToBatteryRepositoryImpl(batteryRepositoryImpl: BatteryRepositoryImpl): BatteryRepository

    @Binds
    fun bindBoostRepositoryToBoostRepositoryImpl(batteryRepositoryImpl: BoostRepositoryImpl): BoostRepository

    @Binds
    fun bindBoostUseCaseImplToBoostUseCase(boostUseCase: BoostUseCaseImpl): BoostUseCase

}