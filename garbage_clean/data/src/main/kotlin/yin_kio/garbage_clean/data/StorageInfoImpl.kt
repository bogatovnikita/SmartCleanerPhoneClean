package yin_kio.garbage_clean.data

import android.content.Context
import yin_kio.garbage_clean.domain.gateways.StorageInfo
import yin_kio.storage_info_provider.StorageInfoProvider

class StorageInfoImpl(
    context: Context
) : StorageInfo {

    private var startVolume = 0L
    private var endVolume = 0L
    private var _freedVolume = 0L


    private val storageInfoProvider = StorageInfoProvider(context)

    override fun saveStartVolume() {
        startVolume = storageInfoProvider.getOccupied()
    }

    override fun saveEndVolume() {
        endVolume = storageInfoProvider.getOccupied()
    }

    override fun calculateFreedVolume() {
        _freedVolume = startVolume - endVolume
    }

    override val freedVolume: Long
        get() = _freedVolume
}