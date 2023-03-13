package com.softcleean.fastcleaner.data.clean_provider

import android.app.Application
import android.content.Context
import com.softcleean.fastcleaner.domain.clean.CleanRepository
import java.lang.ref.WeakReference
import javax.inject.Inject

class CleanRepositoryImpl @Inject constructor(
    private val application: Application
): CleanRepository {

    private val contextWeakRef = WeakReference(application.applicationContext)
    private val context: Context
        get() = contextWeakRef.get()!!

    override fun getGarbageFilesCount(): IntArray = CleanProvider.getGarbageFilesCount(context)

    override fun getGarbageSizeArray(): IntArray = CleanProvider.getGarbageSizeArray(context)

    override fun getFolders(): Array<String> = CleanProvider.getFolders()

    override fun cleanGarbage() = CleanProvider.clean(context)

    override fun getTotalGarbageSize(): Int = CleanProvider.getTotalGarbageSize(context)

    override fun isGarbageCleared(): Boolean = CleanProvider.isGarbageCleared(context)

    override fun getTotalSizeMemory(): Long = CleanProvider.getTotalSizeMemory()

    override fun getUsedSizeMemory(): Long = CleanProvider.getUsedSizeMemory(application)

    override fun getGarbageNameArray(id: Int): Array<String> = context.resources.getStringArray(id)

}