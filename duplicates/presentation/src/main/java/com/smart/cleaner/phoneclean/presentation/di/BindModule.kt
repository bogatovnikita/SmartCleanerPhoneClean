package com.smart.cleaner.phoneclean.presentation.di

import com.smart.cleaner.phoneclean.data.*
import com.smart.cleaner.phoneclean.domain.gateways.*
import com.smart.cleaner.phoneclean.domain.use_case.images.DuplicateImagesUseCase
import com.smart.cleaner.phoneclean.domain.use_case.images.DuplicateImagesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BindModule {

    @Binds
    fun bindFiles(files: FilesImpl): Files

    @Binds
    fun bindImagesComparator(imagesComparator: AndroidImagesComparator): ImagesComparator

    @Binds
    fun bindIFilesComparator(filesComparator: FileComparatorImpl): FilesComparator

    @Binds
    fun bindPermissions(permissions: PermissionsImpl): Permissions

    @Binds
    fun bindDuplicateImagesUseCase(UseCase: DuplicateImagesUseCaseImpl): DuplicateImagesUseCase

    @Binds
    fun bindDuplicatesSettings(settings: SettingsImpl): DuplicatesSettings

}