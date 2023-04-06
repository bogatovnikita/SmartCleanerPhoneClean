package yin_kio.garbage_clean.domain.gateways

interface StorageInfo {

    fun saveStartVolume()
    fun saveEndVolume()
    fun calculateFreedVolume()
    val freedVolume: Long

}