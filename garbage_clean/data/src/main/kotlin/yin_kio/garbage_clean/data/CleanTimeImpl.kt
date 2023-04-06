package yin_kio.garbage_clean.data

import android.content.Context
import yin_kio.garbage_clean.domain.gateways.CleanTime

class CleanTimeImpl(
    context: Context
) : CleanTime {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun saveLastCleanTime() {
        prefs.edit().putLong("CleanTime", System.currentTimeMillis()).apply()
    }

    override fun getLastCleanTime(): Long {
        return prefs.getLong("CleanTime", 0)
    }

    companion object{
        private const val PREFS_NAME = "CleanTime"
    }
}