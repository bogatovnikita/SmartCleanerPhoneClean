package yin_kio.garbage_clean.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import yin_kio.garbage_clean.domain.garbage_files.GarbageFiles
import yin_kio.garbage_clean.domain.gateways.*
import yin_kio.garbage_clean.domain.out.Outer
import yin_kio.garbage_clean.domain.services.DeleteFormMapper
import yin_kio.garbage_clean.domain.use_cases.DeleteUseCase
import yin_kio.garbage_clean.domain.use_cases.GarbageCleanUseCases
import yin_kio.garbage_clean.domain.use_cases.GarbageCleanerUseCasesImpl
import yin_kio.garbage_clean.domain.use_cases.Updater

object GarbageCleanFactory {

    fun createUseCases(
        files: Files,
        outer: Outer,
        coroutineScope: CoroutineScope,
        fileSystemInfoProvider: FileSystemInfoProvider,
        permissions: Permissions,
        ads: Ads,
        noDeletableFiles: NoDeletableFiles
    ) : GarbageCleanUseCases{
        val garbageFiles = GarbageFiles()
        val mapper = DeleteFormMapper()
        val dispatcher = Dispatchers.IO

        val updater = Updater(
            outer = outer,
            coroutineScope = coroutineScope,
            mapper = mapper,
            garbageFiles = garbageFiles,
            files = files,
            fileSystemInfoProvider = fileSystemInfoProvider,
            permissions = permissions,
            dispatcher = dispatcher,
            noDeletableFiles = noDeletableFiles
        )

        val deleteUseCase = DeleteUseCase(
            garbageFiles = garbageFiles,
            ads = ads,
            coroutineScope = coroutineScope,
            dispatcher = dispatcher,
            files = files,
            noDeletableFiles = noDeletableFiles,
            outer = outer,
        )

        return GarbageCleanerUseCasesImpl(
            garbageFiles = garbageFiles,
            mapper = mapper,
            outer = outer,
            coroutineScope = coroutineScope,
            dispatcher = Dispatchers.Default,
            updater =  updater,
            deleteUseCase = deleteUseCase
        )
    }

}