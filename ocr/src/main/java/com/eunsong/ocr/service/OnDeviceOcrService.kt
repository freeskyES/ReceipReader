package com.eunsong.ocr.service

import com.eunsong.ocr.model.OcrResult
import jakarta.inject.Inject

class OnDeviceOcrService @Inject constructor(): OcrService {
//    private val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())

    override suspend fun recognizeText(imagePath: String): OcrResult {
        return OcrResult(text = "Hello", blocks = listOf("Hello"))
//        val bitmap = BitmapFactory.decodeFile(imagePath)
//        val inputImage = InputImage.fromBitmap(bitmap, 0)
//
//        return suspendCoroutine { continuation ->
//            recognizer.process(inputImage)
//                .addOnSuccessListener { visionText ->
//                    continuation.resume(OcrResult(
//                        text = visionText.text,
//                        blocks = visionText.textBlocks.map { it.text }
//                    ))
//                }
//                .addOnFailureListener { e ->
//                    continuation.resumeWithException(e)
//                }
//        }
    }
}
