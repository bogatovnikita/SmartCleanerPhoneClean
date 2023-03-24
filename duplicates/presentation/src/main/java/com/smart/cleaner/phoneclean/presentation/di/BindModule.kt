package com.smart.cleaner.phoneclean.presentation.di

import com.smart.cleaner.phoneclean.data.AndroidImagesComparator
import com.smart.cleaner.phoneclean.data.FilesImpl
import com.smart.cleaner.phoneclean.data.PermissionsImpl
import com.smart.cleaner.phoneclean.domain.gateways.Files
import com.smart.cleaner.phoneclean.domain.gateways.ImagesComparator
import com.smart.cleaner.phoneclean.domain.gateways.Permissions
import com.smart.cleaner.phoneclean.domain.use_case.images.DuplicateImagesUseCase
import com.smart.cleaner.phoneclean.domain.use_case.images.DuplicateImagesUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface BindModule {

    @Binds
    fun bindFiles(files: FilesImpl): Files

    @Binds
    fun bindIma(imagesComparator: AndroidImagesComparator): ImagesComparator

    @Binds
    fun bindIma(permissions: PermissionsImpl): Permissions

    @Binds
    fun bindIma(UseCase: DuplicateImagesUseCaseImpl): DuplicateImagesUseCase

}