package com.entertainment.event.ssearch.data.cooling_provider

import android.app.Application
import com.entertainment.event.ssearch.domain.cooling.CoolingRepository
import javax.inject.Inject

class CoolingRepositoryImpl @Inject constructor(
    private val application: Application
) : CoolingRepository {

    override fun calculateTemperature(temperature: Int): Int =
        CoolingProvider.calculateTemperature(application, temperature)

    override fun getCoolingDone(): Boolean = CoolingProvider.getOverheatedApps(application) == 0

    override fun cpu() = CoolingProvider.cpu(application)

}