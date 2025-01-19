package com.eunsong.receiptreader.ui

import android.graphics.Bitmap

sealed class HomeIntent {
    object OpenCamera : HomeIntent() // 카메라 열기
    object CloseCamera : HomeIntent() // 카메라 닫기
    data class CaptureImage(val bitmap: Bitmap) : HomeIntent() // 이미지 캡처
    data class ShowError(val message: String) : HomeIntent() // 에러 표시
}
