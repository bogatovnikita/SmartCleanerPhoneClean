package yin_kio.garbage_clean.domain.services.garbage_files

import java.io.File

object Extensions {

    private val separator = File.separator

    const val APK = ".apk"
    const val TMP = ".tmp"
    const val TEMP = ".temp"
    val TEMP_PATH = "${separator}temp$separator"
    val TMP_PATH = "${separator}tmp$separator"
    const val DAT = ".dat"
    const val LOG = ".log"
    val LOG_PATH = "${separator}log$separator"
    const val THUMB = ".thumb"
    val THUMB_PATH = "${separator}thumb$separator"
    val THUMBNAILS_PATH = "${separator}thumbnails$separator"

}