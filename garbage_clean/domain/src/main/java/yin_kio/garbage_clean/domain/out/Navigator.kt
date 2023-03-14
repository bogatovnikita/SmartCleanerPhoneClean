package yin_kio.garbage_clean.domain.out

interface Navigator {

    fun showPermission()
    fun showDeleteProgress()
    fun showInter()
    fun complete()
    fun close()

}