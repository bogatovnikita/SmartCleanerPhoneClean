package yin_kio.garbage_clean.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import yin_kio.garbage_clean.domain.actions.CleanAction
import yin_kio.garbage_clean.domain.actions.UpdateAction
import yin_kio.garbage_clean.domain.entities.GarbageSelectorImpl
import yin_kio.garbage_clean.domain.gateways.CleanTime
import yin_kio.garbage_clean.domain.gateways.Files
import yin_kio.garbage_clean.domain.gateways.Permissions
import yin_kio.garbage_clean.domain.gateways.StorageInfo
import yin_kio.garbage_clean.domain.services.CleanTracker
import yin_kio.garbage_clean.domain.services.garbage_forms_provider.GarbageFormsProviderImpl
import yin_kio.garbage_clean.domain.ui_out.UiOuter
import yin_kio.garbage_clean.domain.ui_out.garbage_out_creator.GarbageOutCreatorImpl
import yin_kio.garbage_clean.domain.use_case.GarbageFilesUseCase
import yin_kio.garbage_clean.domain.use_case.GarbageFilesUseCaseImpl

object GarbageFilesFactory {

    fun createGarbageFilesUseCases(
        uiOuter: UiOuter,
        permissions: Permissions,
        files: Files,
        storageInfo: StorageInfo,
        cleanTime: CleanTime,
        coroutineScope: CoroutineScope,
    ) : GarbageFilesUseCase{

        val garbageSelector = GarbageSelectorImpl()


        val garbageOutCreator = GarbageOutCreatorImpl()
        val cleanTracker = CleanTracker(cleanTime)

        val updateAction = UpdateAction(
            uiOuter = uiOuter,
            garbageSelector = garbageSelector,
            garbageFormsProvider = GarbageFormsProviderImpl(files),
            garbageOutCreator = garbageOutCreator,
            cleanTracker = cleanTracker
        )

        val cleanAction = CleanAction(
            storageInfo = storageInfo,
            uiOuter = uiOuter,
            files = files,
            garbageSelector = garbageSelector,
            cleanTime = cleanTime
        )


        return GarbageFilesUseCaseImpl(
            uiOuter = uiOuter,
            garbageSelector = garbageSelector,
            permissions = permissions,
            updateAction = updateAction,
            storageInfo = storageInfo,
            coroutineScope = coroutineScope,
            dispatcher = Dispatchers.IO,
            cleanAction = cleanAction,
        )

    }

}