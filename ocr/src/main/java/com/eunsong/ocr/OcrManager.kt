package com.eunsong.ocr

import android.graphics.Bitmap
import com.eunsong.ocr.model.OcrResult
import com.eunsong.ocr.service.CloudOcrService
import com.eunsong.ocr.service.OcrService
import com.eunsong.ocr.service.OnDeviceOcrService

class OcrManager(
    private val onDeviceOcrService: OcrService = OnDeviceOcrService(),
    private val cloudOcrService: OcrService = CloudOcrService()
) {
    suspend fun recognizeText(bitmap: Bitmap, useCloud: Boolean = false): OcrResult {
        return if (useCloud) {
            cloudOcrService.recognizeText(bitmap)
        } else {
            onDeviceOcrService.recognizeText(bitmap)
        }
    }
}
