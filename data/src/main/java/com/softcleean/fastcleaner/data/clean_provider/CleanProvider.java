package com.softcleean.fastcleaner.data.clean_provider;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import androidx.annotation.Keep;

import java.io.File;

@Keep
public class CleanProvider {

    static{
        System.loadLibrary("clean-lib");
    }

    public static native int[] getGarbageFilesCount(Context context);

    //Return size by megabytes
    public static native int[] getGarbageSizeArray(Context context);

    public static native String[] getFolders();

    public static native void clean(Context context);

    //Return size by megabytes
    public static int getTotalGarbageSize(Context context) {
        int[] listGarbageSize = getGarbageSizeArray(context);
        int totalSize = 0;
        for (int size:  listGarbageSize) {
            totalSize += size;
        }
        return totalSize;
    }

    //Return size by bytes
    public static Long getTotalSizeMemory() {
        File externalStorageFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        StatFs stats = new StatFs(externalStorageFolder.getAbsolutePath());
        return stats.getTotalBytes();
    }

    //Return size by bytes
    public static Long getUsedSizeMemory(Context context) {
        File externalStorageFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        StatFs stats = new StatFs(externalStorageFolder.getAbsolutePath());
        long usedMemory = stats.getTotalBytes() - stats.getFreeBytes() + (long) getTotalGarbageSize(context) * 1024 * 1024;
        return Math.min(usedMemory, stats.getTotalBytes());
    }

    public static boolean isGarbageCleared(Context context) {
        return getTotalGarbageSize(context) == 0;
    }

}
