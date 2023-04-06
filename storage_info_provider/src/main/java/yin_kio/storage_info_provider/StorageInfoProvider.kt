package yin_kio.storage_info_provider

import android.app.usage.StorageStatsManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.storage.StorageManager
import androidx.annotation.RequiresApi

class StorageInfoProvider(
    private val context: Context
) {

    fun getStorageInfo() : StorageInfo{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val storageStatsManager = storageStatsManager()
            val total = storageStatsManager.getTotalBytes(StorageManager.UUID_DEFAULT)
            val free = storageStatsManager.getFreeBytes(StorageManager.UUID_DEFAULT)
            val occupied = total - free

            StorageInfo(
                occupied = occupied,
                total = total,
                free = free
            )
        } else {
            val stats = statFs()
            val total = stats.totalBytes
            val free = stats.freeBytes
            val occupied = total - free
            StorageInfo(
                occupied = occupied,
                total = total,
                free = free
            )
        }
    }

    private fun statFs() = StatFs(Environment.getExternalStorageDirectory().absolutePath)

    @RequiresApi(Build.VERSION_CODES.O)
    private fun storageStatsManager() =
        context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager

    fun getTotal() : Long{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            storageStatsManager().getTotalBytes(StorageManager.UUID_DEFAULT)
        } else {
           statFs().totalBytes
        }
    }

    fun getOccupied() : Long {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ssm = storageStatsManager()
            val total = ssm.getTotalBytes(StorageManager.UUID_DEFAULT)
            val free = ssm.getFreeBytes(StorageManager.UUID_DEFAULT)
            total - free
        } else {
            val ssm = statFs()
            val total = ssm.totalBytes
            val free = ssm.freeBytes
            total - free
        }
    }
}