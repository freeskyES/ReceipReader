package com.eunsong.ocr.model

data class OcrResult(
    val text: String,
    val blocks: List<String>
)