package yin_kio.garbage_clean.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import yin_kio.garbage_clean.domain.entities.GarbageFiles
import yin_kio.garbage_clean.domain.gateways.*
import yin_kio.garbage_clean.domain.out.Outer
import yin_kio.garbage_clean.domain.services.DeleteFormMapper
import yin_kio.garbage_clean.domain.use_cases.GarbageCleanUseCases
import yin_kio.garbage_clean.domain.use_cases.GarbageCleanerUseCasesImpl
import yin_kio.garbage_clean.domain.use_cases.UpdateUseCase

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

        return GarbageCleanerUseCasesImpl(
            garbageFiles = garbageFiles,
            mapper = mapper,
            files = files,
            outer = outer,
            coroutineScope = coroutineScope,
            dispatcher = Dispatchers.Default,
            updateUseCase =  UpdateUseCase(
                outer = outer,
                coroutineScope = coroutineScope,
                mapper = mapper,
                garbageFiles = garbageFiles,
                files = files,
                fileSystemInfoProvider = fileSystemInfoProvider,
                permissions = permissions,
                dispatcher = Dispatchers.IO,
                noDeletableFiles = noDeletableFiles
            ),
            ads = ads,
            noDeletableFiles = noDeletableFiles
        )
    }

}