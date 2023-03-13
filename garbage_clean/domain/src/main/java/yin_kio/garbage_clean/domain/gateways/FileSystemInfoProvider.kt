package yin_kio.garbage_clean.domain.gateways

import yin_kio.garbage_clean.domain.entities.FileSystemInfo

interface FileSystemInfoProvider {

    suspend fun getFileSystemInfo() : FileSystemInfo

}