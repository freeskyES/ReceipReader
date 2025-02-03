package com.eunsong.camerax

interface CameraCapture {

    fun startCamera()
    fun stopCamera()

    // 이미지 캡처: 결과 또는 에러를 비동기로 반환
    suspend fun captureImage(): Result<String>
}