package com.eunsong.receiptreader.ui.home

import android.graphics.Bitmap

sealed class HomeState {
    object Idle : HomeState() // 기본 상태
    object CameraOpen : HomeState() // 카메라 화면
    data class ImageCaptured(val bitmap: Bitmap) : HomeState() // 이미지 캡처
    data class Error(val message: String) : HomeState() // 에러
}
