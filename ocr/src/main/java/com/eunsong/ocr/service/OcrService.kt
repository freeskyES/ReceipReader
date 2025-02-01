package com.eunsong.ocr.service

import android.graphics.Bitmap
import com.eunsong.ocr.model.OcrResult

interface OcrService {
    suspend fun recognizeText(bitmap: Bitmap): OcrResult
}