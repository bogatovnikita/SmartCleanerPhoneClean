package com.softcleean.fastcleaner.di

import com.softcleean.fastcleaner.data.battery_provider.BatteryRepositoryImpl
import com.softcleean.fastcleaner.data.boost_provider.BoostRepositoryImpl
import com.softcleean.fastcleaner.data.clean_provider.CleanRepositoryImpl
import com.softcleean.fastcleaner.data.cooling_provider.CoolingRepositoryImpl
import com.softcleean.fastcleaner.domain.battery.BatteryRepository
import com.softcleean.fastcleaner.domain.boost.BoostRepository
import com.softcleean.fastcleaner.domain.clean.CleanRepository
import com.softcleean.fastcleaner.domain.cooling.CoolingRepository
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
    fun bindCoolingRepositoryToCoolingRepositoryImpl(coolingRepositoryImpl: CoolingRepositoryImpl): CoolingRepository

    @Binds
    fun bindCleanRepositoryToCleanRepositoryImpl(coolingRepositoryImpl: CleanRepositoryImpl): CleanRepository

}