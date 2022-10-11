package com.entertainment.event.ssearch.data.cooling_provider

import android.app.Application
import android.content.Context
import com.entertainment.event.ssearch.domain.cooling.CoolingRepository
import java.lang.ref.WeakReference
import javax.inject.Inject

class CoolingRepositoryImpl @Inject constructor(
    private val application: Application
) : CoolingRepository {

    private val contextWeakRef = WeakReference(application.applicationContext)
    private val context: Context
        get() = contextWeakRef.get()!!

    override fun calculateTemperature(temperature: Int): Int =
        CoolingProvider.calculateTemperature(context, temperature)

    override fun getCoolingDone(): Boolean = CoolingProvider.getOverheatedApps(context) == 0

    override fun cpu() = CoolingProvider.cpu(context)

}