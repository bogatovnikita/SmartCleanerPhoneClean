package yin_kio.garbage_clean.domain.gateways

interface Files {

    suspend fun deleteAndGetNoDeletable(paths: List<String>) : List<String>
    suspend fun getAll() : List<String>

}