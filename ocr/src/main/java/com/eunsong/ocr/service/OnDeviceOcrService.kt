package com.eunsong.ocr.service

import android.graphics.Bitmap
import com.eunsong.ocr.model.OcrResult
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import kotlinx.coroutines.tasks.await

class OnDeviceOcrService : OcrService {
    private val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())

    override suspend fun recognizeText(bitmap: Bitmap): OcrResult {
        val image = InputImage.fromBitmap(bitmap, 0)
        val result = recognizer.process(image).await()
        
        return OcrResult(
            text = result.text,
            blocks = result.textBlocks.map { it.text }
        )
    }
}
