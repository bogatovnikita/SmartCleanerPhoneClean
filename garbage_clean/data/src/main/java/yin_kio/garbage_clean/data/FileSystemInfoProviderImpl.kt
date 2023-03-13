package yin_kio.garbage_clean.data

import android.content.Context
import yin_kio.garbage_clean.domain.entities.FileSystemInfo
import yin_kio.garbage_clean.domain.gateways.FileSystemInfoProvider
import yin_kio.storage_info_provider.StorageInfoProvider

class FileSystemInfoProviderImpl(
    context: Context
) : FileSystemInfoProvider {

    private val storageInfoProvider = StorageInfoProvider(context)

    override suspend fun getFileSystemInfo(): FileSystemInfo {
        val storageInfo = storageInfoProvider.getStorageInfo()

        return FileSystemInfo(
            occupied = storageInfo.occupied,
            available = storageInfo.free,
            total = storageInfo.total
        )
    }
}