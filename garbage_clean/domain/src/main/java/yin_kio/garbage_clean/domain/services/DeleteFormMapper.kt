package yin_kio.garbage_clean.domain.services

import yin_kio.garbage_clean.domain.entities.DeleteForm
import yin_kio.garbage_clean.domain.out.DeleteFormOut
import yin_kio.garbage_clean.domain.out.DeleteFormOutItem

internal class DeleteFormMapper {

    fun createDeleteFormOut(deleteForm: DeleteForm) : DeleteFormOut {
        return DeleteFormOut(
            isAllSelected = deleteForm.isAllSelected,
            canDelete = deleteForm.deleteRequest.isNotEmpty(),
            items = deleteForm.map {
                DeleteFormOutItem(
                    garbageType = it.garbageType,
                    size = it.size,
                    isSelected = deleteForm.isAllSelected || deleteForm.deleteRequest.contains(it.garbageType)
                )
            }
        )
    }

}