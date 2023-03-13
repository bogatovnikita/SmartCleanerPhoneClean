package yin_kio.garbage_clean.domain.out

import yin_kio.garbage_clean.domain.entities.FileSystemInfo
import yin_kio.garbage_clean.domain.entities.GarbageType

interface OutBoundary {

    fun outUpdateProgress(isInProgress: Boolean)
    fun outDeleteForm(deleteFormOut: DeleteFormOut)
    fun outFileSystemInfo(fileSystemInfo: FileSystemInfo)
    fun outHasPermission(hasPermission: Boolean)
    fun outDeleteProgress(deleteProgressState: DeleteProgressState)
    fun outDeleteRequest(deleteRequest: List<GarbageType>)
    fun outIsClosed(isClosed: Boolean)
    fun outDeletedSize(size: Long)

}