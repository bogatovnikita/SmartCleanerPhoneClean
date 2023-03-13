package com.softcleean.fastcleaner.data.boost_provider

import android.app.Application
import android.content.Context
import com.softcleean.fastcleaner.domain.boost.BoostRepository
import java.lang.ref.WeakReference
import javax.inject.Inject


class BoostRepositoryImpl @Inject constructor(
    private val application: Application
) : BoostRepository {

    private val contextWeakRef = WeakReference(application.applicationContext)
    private val context: Context
        get() = contextWeakRef.get()!!

    override fun getTotalRam(): Long = BoostProvider.getRamTotal(context)

    override fun getRamUsage(): Long =
        BoostProvider.getRamUsage(
            context,
            BoostProvider.getRamTotal(context),
            BoostProvider.getRamPart(context)
        )

    override fun getOverloadedPercents(): Int = BoostProvider.getOverloadedPercents(context)

    override fun boost() = BoostProvider.boost(context)

    override fun checkRamOverload(): Boolean = BoostProvider.checkRamOverload(context)

}