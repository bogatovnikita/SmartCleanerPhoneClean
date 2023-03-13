package yin_kio.garbage_clean.presentation.view_model

import yin_kio.garbage_clean.domain.entities.GarbageType
import yin_kio.garbage_clean.domain.out.DeleteProgressState
import yin_kio.garbage_clean.presentation.models.UiDeleteFromItem
import yin_kio.garbage_clean.presentation.models.UiFileSystemInfo

interface MutableScreenViewModel {

    fun setFileSystemInfo(uiFileSystemInfo: UiFileSystemInfo)
    fun setIsInProgress(isInProgress: Boolean)
    fun setHasPermission(hasPermission: Boolean)
    fun setDeleteProgress(deleteProgressState: DeleteProgressState)
    fun setDeleteFormItems(deleteFormItems: List<UiDeleteFromItem>)
    fun setIsAllSelected(isAllSelected: Boolean)
    fun setCanFreeSize(canFreeVolume: String)
    fun setButtonText(text: String)
    fun setButtonBgRes(bgRes: Int)
    fun setDeleteRequest(deleteRequest: List<GarbageType>)
    fun setIsClosed(isClosed: Boolean)
    fun setDeletedSize(size: String)

}