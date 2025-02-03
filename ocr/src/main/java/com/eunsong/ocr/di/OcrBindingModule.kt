package com.eunsong.ocr.di

import com.eunsong.ocr.service.CloudOcrService
import com.eunsong.ocr.service.OcrService
import com.eunsong.ocr.service.OnDeviceOcrService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class OcrBindingModule {

    @Binds
    abstract fun bindOnDeviceOcrService(service: OnDeviceOcrService): OcrService

    @Binds
    abstract fun bindCloudOcrService(service: CloudOcrService): OcrService
}