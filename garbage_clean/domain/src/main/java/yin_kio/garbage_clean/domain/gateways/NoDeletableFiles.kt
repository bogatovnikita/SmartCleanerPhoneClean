package yin_kio.garbage_clean.domain.gateways

interface NoDeletableFiles {
    fun get() : Set<String>
    fun save(paths: List<String>)
}