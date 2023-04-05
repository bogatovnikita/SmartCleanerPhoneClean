package com.smart.cleaner.phoneclean.domain.use_case.files

import com.smart.cleaner.phoneclean.domain.models.File


interface DuplicateFilesUseCase {

    suspend fun getFileDuplicates(): List<List<File>>

}