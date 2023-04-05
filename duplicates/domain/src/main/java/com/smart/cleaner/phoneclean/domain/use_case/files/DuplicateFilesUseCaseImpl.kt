package com.smart.cleaner.phoneclean.domain.use_case.files

import com.smart.cleaner.phoneclean.domain.extentions.findDuplicates
import com.smart.cleaner.phoneclean.domain.gateways.*
import com.smart.cleaner.phoneclean.domain.models.File
import com.smart.cleaner.phoneclean.domain.use_case.BaseDuplicateUseCase
import javax.inject.Inject

class DuplicateFilesUseCaseImpl @Inject constructor(
    private val files: Files,
    private val permissions: Permissions,
    private val settings: DuplicatesSettings,
    private val filesComparator: FilesComparator,
) : BaseDuplicateUseCase(files, permissions, settings), DuplicateFilesUseCase {

    override suspend fun getFileDuplicates(): List<List<File>> =
        files.getFiles().findDuplicates(filesComparator)

}