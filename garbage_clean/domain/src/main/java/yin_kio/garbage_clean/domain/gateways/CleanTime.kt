package yin_kio.garbage_clean.domain.gateways

interface CleanTime {

    fun saveLastCleanTime()
    fun getLastCleanTime() : Long

}