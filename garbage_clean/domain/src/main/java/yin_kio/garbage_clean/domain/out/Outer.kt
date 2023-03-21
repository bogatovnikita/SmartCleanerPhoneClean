package yin_kio.garbage_clean.domain.out

import yin_kio.garbage_clean.domain.entities.FileSystemInfo
import yin_kio.garbage_clean.domain.entities.GarbageType

interface Outer {

    fun outUpdateProgress(isInProgress: Boolean)
    fun outDeleteForm(deleteFormOut: DeleteFormOut)
    fun outFileSystemInfo(fileSystemInfo: FileSystemInfo)
    fun outDeleteRequest(deleteRequest: List<GarbageType>)
    fun outDeletedSize(size: Long)

}