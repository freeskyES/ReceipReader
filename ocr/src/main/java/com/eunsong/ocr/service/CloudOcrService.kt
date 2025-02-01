package com.eunsong.ocr.service

import android.graphics.Bitmap
import android.util.Base64
import com.eunsong.ocr.model.OcrResult
import com.google.firebase.functions.FirebaseFunctions
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class CloudOcrService : OcrService {
    private val functions = FirebaseFunctions.getInstance()

    override suspend fun recognizeText(bitmap: Bitmap): OcrResult {
        val base64Image = encodeToBase64(bitmap)

        val request = JsonObject().apply {
            addProperty("image", base64Image)
            add("features", JsonObject().apply { addProperty("type", "TEXT_DETECTION") })
        }

        val response = functions
            .getHttpsCallable("annotateImage")
            .call(request.toString())
            .await()

        val jsonResponse = JsonParser.parseString(response.getData().toString()).asJsonObject
        val recognizedText = jsonResponse["fullTextAnnotation"].asJsonObject["text"].asString

        return OcrResult(text = recognizedText, blocks = listOf(recognizedText))
    }

    private fun encodeToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP)
    }
}
