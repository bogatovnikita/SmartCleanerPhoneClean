package yin_kio.garbage_clean.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import yin_kio.garbage_clean.domain.entities.GarbageSelectorImpl
import yin_kio.garbage_clean.domain.gateways.Files
import yin_kio.garbage_clean.domain.gateways.Permissions
import yin_kio.garbage_clean.domain.gateways.StorageInfo
import yin_kio.garbage_clean.domain.services.garbage_forms_provider.GarbageFormsProviderImpl
import yin_kio.garbage_clean.domain.ui_out.UiOuter
import yin_kio.garbage_clean.domain.ui_out.garbage_out_creator.GarbageOutCreatorImpl
import yin_kio.garbage_clean.domain.use_cases.*
import yin_kio.garbage_clean.domain.use_cases.CleanUseCase
import yin_kio.garbage_clean.domain.use_cases.GarbageFilesUseCasesImpl
import yin_kio.garbage_clean.domain.use_cases.UpdateUseCase

object GarbageFilesFactory {

    fun createGarbageFilesUseCases(
        uiOuter: UiOuter,
        permissions: Permissions,
        files: Files,
        storageInfo: StorageInfo,
        coroutineScope: CoroutineScope,
    ) : GarbageFilesUseCases{

        val garbageSelector = GarbageSelectorImpl()

        val updateState = UpdateStateHolder()

        val updateUseCase = UpdateUseCase(
            uiOuter = uiOuter,
            garbageSelector = garbageSelector,
            garbageFormsProvider = GarbageFormsProviderImpl(files),
            garbageOutCreator = GarbageOutCreatorImpl(),
            updateState = updateState
        )

        val cleanUseCase = CleanUseCase(
            storageInfo = storageInfo,
            uiOuter = uiOuter,
            files = files,
            garbageSelector = garbageSelector
        )


        val scanUseCase = ScanUseCase(
            permissions = permissions,
            updateUseCase = updateUseCase,
            uiOuter = uiOuter,
        )

        return GarbageFilesUseCasesImpl(
            uiOuter = uiOuter,
            garbageSelector = garbageSelector,
            permissions = permissions,
            updateUseCase = updateUseCase,
            storageInfo = storageInfo,
            coroutineScope = coroutineScope,
            dispatcher = Dispatchers.IO,
            cleanUseCase = cleanUseCase,
            scanUseCase = scanUseCase,
            updateState = updateState
        )

    }

}