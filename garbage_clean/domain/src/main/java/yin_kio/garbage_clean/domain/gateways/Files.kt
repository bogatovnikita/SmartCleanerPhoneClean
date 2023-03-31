package yin_kio.garbage_clean.domain.gateways

import java.io.File

interface Files {

    fun getAllFiles() : List<File>

}