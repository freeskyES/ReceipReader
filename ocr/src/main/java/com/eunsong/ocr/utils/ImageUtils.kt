package com.eunsong.ocr.utils

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

object ImageUtils {
    fun scaleBitmapDown(bitmap: Bitmap, maxDimension: Int): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        val resizedWidth: Int
        val resizedHeight: Int

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension
            resizedWidth = (resizedHeight * originalWidth.toFloat() / originalHeight).toInt()
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension
            resizedHeight = (resizedWidth * originalHeight.toFloat() / originalWidth).toInt()
        } else {
            resizedWidth = maxDimension
            resizedHeight = maxDimension
        }

        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false)
    }

    fun encodeToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP)
    }
}
