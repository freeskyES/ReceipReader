//package com.eunsong.ocr.service
//
//import com.eunsong.ocr.model.OcrRequest
//import com.google.firebase.functions.FirebaseFunctions
//import kotlinx.coroutines.tasks.await
//
//class FirebaseOcrService(
//    private val functions: FirebaseFunctions
//) : OcrService {
////    override suspend fun recognizeText(imageData: ByteArray): OcrResult {
////        return OcrResult(extractTextFromImage(imageData))
////    }
//
//    private suspend fun extractTextFromImage(base64Image: String): String {
//        val request = OcrRequest(imageContent = base64Image)
//        val result = functions
//            .getHttpsCallable("annotateImage")
//            .call(request.toMap())
//            .await()
//        return result.getData().toString() // 실제 텍스트 추출 결과 반환
//    }
//}