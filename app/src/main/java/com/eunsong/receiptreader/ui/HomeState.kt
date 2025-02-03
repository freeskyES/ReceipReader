package com.eunsong.receiptreader.ui

sealed class HomeState {
    object Idle : HomeState()
    object CameraOpen : HomeState()
    data class ImageCaptured(val imagePath: String) : HomeState()
    data class OcrProcessing(val imagePath: String) : HomeState()
    data class OcrSuccess(val text: String) : HomeState()
    data class Error(val message: String) : HomeState()
}
