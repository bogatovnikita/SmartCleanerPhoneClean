package com.entertainment.event.ssearch.ar155.di

import com.entertainment.event.ssearch.data.battery_repository.BatteryRepositoryImpl
import com.entertainment.event.ssearch.data.boost_provider.BoostRepositoryImpl
import com.entertainment.event.ssearch.data.cooling_provider.CoolingRepositoryImpl
import com.entertainment.event.ssearch.domain.battery.BatteryRepository
import com.entertainment.event.ssearch.domain.boost.BoostRepository
import com.entertainment.event.ssearch.domain.cooling.CoolingRepository
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

}