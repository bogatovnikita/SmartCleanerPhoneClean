package yin_kio.garbage_clean.domain.services

import yin_kio.garbage_clean.domain.gateways.CleanTime
import java.util.concurrent.TimeUnit

internal class CleanTracker(
    private val cleanTime: CleanTime
) {

    val wasClean: Boolean get() {
        val delta = System.currentTimeMillis() - cleanTime.getLastCleanTime()
        return delta < TimeUnit.DAYS.toMillis(1)
    }

}