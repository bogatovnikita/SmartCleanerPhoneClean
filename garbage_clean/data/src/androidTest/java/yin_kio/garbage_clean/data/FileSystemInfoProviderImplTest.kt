package yin_kio.garbage_clean.data

import android.os.Environment
import android.os.StatFs
import android.text.format.Formatter.formatFileSize
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
internal class FileSystemInfoProviderImplTest{

    @Test
    fun test(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext // This tests does not work with OlejaAds lib

//        val storageStatsManager = context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
//        val totalBytes = storageStatsManager.getTotalBytes(StorageManager.UUID_DEFAULT)
//        val availableBytes = storageStatsManager.getFreeBytes(StorageManager.UUID_DEFAULT)
//
//        println("totalSize: ${formatFileSize(context, totalBytes)}")
//        println("availableSize: ${formatFileSize(context, availableBytes)}")
//
        val directories = arrayOf(
//            Environment.getDownloadCacheDirectory(),
            Environment.getStorageDirectory(),
            Environment.getRootDirectory(),
            Environment.getExternalStorageDirectory(),
            File("")
//            Environment.getDownloadCacheDirectory(),
        )

        println(android.os.Build.HARDWARE)

        val totalSize = directories.sumOf {
            println("$it: ${formatFileSize(context, it.totalSpace)}")
            StatFs(it.absolutePath).totalBytes }
        println("totalSize: ${formatFileSize(context, totalSize)}")
    }

}