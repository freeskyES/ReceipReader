package com.eunsong.ocr.di

import com.eunsong.ocr.OcrManager
import com.eunsong.ocr.service.CloudOcrService
import com.eunsong.ocr.service.OnDeviceOcrService
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OcrProvidingModule {

    @Provides
    @Singleton
    fun provideFirebaseFunctions(): FirebaseFunctions {
        return Firebase.functions
    }

    @Provides
    @Singleton
    fun provideOnDeviceOcrService(): OnDeviceOcrService {
        return OnDeviceOcrService()
    }

    @Provides
    @Singleton
    fun provideCloudOcrService(firebaseFunctions: FirebaseFunctions): CloudOcrService {
        return CloudOcrService(firebaseFunctions)
    }

    @Provides
    @Singleton
    fun provideOcrManager(
        onDeviceOcrService: OnDeviceOcrService,
        cloudOcrService: CloudOcrService
    ): OcrManager {
        return OcrManager(onDeviceOcrService, cloudOcrService)
    }
}
