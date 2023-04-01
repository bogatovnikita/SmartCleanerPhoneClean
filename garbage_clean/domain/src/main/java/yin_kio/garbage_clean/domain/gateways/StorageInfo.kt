package yin_kio.garbage_clean.domain.gateways

interface StorageInfo {

    fun saveStartVolume()
    fun saveEndVolume()
    fun calculateEndVolume()
    val freedVolume: Long

}