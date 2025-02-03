package com.eunsong.ocr.service

import com.eunsong.ocr.model.OcrResult

interface OcrService {
    suspend fun recognizeText(imagePath: String): OcrResult
}