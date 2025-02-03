package com.eunsong.ocr.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.eunsong.ocr.model.OcrResult
import com.google.firebase.functions.FirebaseFunctions
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.File

class CloudOcrService @Inject constructor(
    private val firebaseFunctions: FirebaseFunctions
) : OcrService {

    override suspend fun recognizeText(imagePath: String): OcrResult {
        val base64Image = encodeToBase64(imagePath)
        val requestJson = createCloudVisionRequest(base64Image)
//        val request = JsonObject().apply {
//            addProperty("image", base64Image)
//            add("features", JsonObject().apply { addProperty("type", "TEXT_DETECTION") })
//        }

        val response = firebaseFunctions
            .getHttpsCallable("annotateImage")
            .call(requestJson)
            .await()

        val resultJson = response.getData() as Map<*, *>
        val recognizedText = resultJson["textAnnotations"]?.toString() ?: "No Text Found"

//        val jsonResponse = JsonParser.parseString(response.getData().toString()).asJsonObject
//        val recognizedText = jsonResponse["fullTextAnnotation"].asJsonObject["text"].asString

        return OcrResult(text = recognizedText, blocks = listOf(recognizedText))
    }

    private fun createCloudVisionRequest(base64Image: String): String {
        val request = JsonObject()

        // 이미지 데이터를 추가
        val image = JsonObject()
        image.add("content", JsonPrimitive(base64Image))
        request.add("image", image)

        // OCR 요청 유형 (TEXT_DETECTION 또는 DOCUMENT_TEXT_DETECTION)
        val feature = JsonObject()
        feature.add("type", JsonPrimitive("TEXT_DETECTION"))

        val features = JsonArray()
        features.add(feature)
        request.add("features", features)

        // (선택 사항) 언어 힌트 추가
        val imageContext = JsonObject()
        val languageHints = JsonArray()
        languageHints.add("en") // 영어 OCR
        imageContext.add("languageHints", languageHints)
        request.add("imageContext", imageContext)

        return request.toString()
    }

    private fun encodeToBase64(imagePath: String): String {
        val file = File(imagePath)
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP)
    }
}
