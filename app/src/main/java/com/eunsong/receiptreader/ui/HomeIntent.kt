package com.eunsong.receiptreader.ui

sealed class HomeIntent {
    object OpenCamera : HomeIntent()      // 카메라 열기
    object OpenGallery : HomeIntent()     // 갤러리 열기
    object CloseCamera : HomeIntent()     // 카메라 종료

    data class ImageCaptured(val imagePath: String) : HomeIntent()  // 이미지가 캡처됨
    data class ImageSelected(val imagePath: String) : HomeIntent()  // 갤러리에서 이미지 선택

    data class ProcessOcr(val imagePath: String) : HomeIntent()      // OCR 프로세스 실행
    data class OcrCompleted(val text: String) : HomeIntent()        // OCR 결과를 받음
    data class OcrFailed(val error: String) : HomeIntent()          // OCR 실패
}
