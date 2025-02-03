package com.eunsong.ocr

import com.eunsong.ocr.model.OcrResult
import com.eunsong.ocr.service.CloudOcrService
import com.eunsong.ocr.service.OnDeviceOcrService
import jakarta.inject.Inject
import timber.log.Timber

class OcrManager @Inject constructor(
    private val onDeviceOcrService: OnDeviceOcrService,
    private val cloudOcrService: CloudOcrService
) {
    suspend fun recognizeText(imagePath: String, useCloud: Boolean = false): OcrResult {
        Timber.plant(Timber.DebugTree())
        return if (useCloud) {
            cloudOcrService.recognizeText(imagePath)
        } else {
            onDeviceOcrService.recognizeText(imagePath)
        }
    }
}
