package com.smart.cleaner.phoneclean.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import yin_kio.file_utils.FileUtils
import yin_kio.file_utils.FileUtilsImpl
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ProviderModule {

    @Singleton
    @Provides
    fun provideFileUtil(): FileUtils = FileUtilsImpl()

}