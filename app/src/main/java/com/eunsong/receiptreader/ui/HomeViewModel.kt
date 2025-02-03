package com.eunsong.receiptreader.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eunsong.ocr.OcrManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ocrManager: OcrManager,
//    private val cameraManager: CameraCaptureManager
) : ViewModel() {
    private val _state = MutableStateFlow<HomeState>(HomeState.Idle)
    val state: StateFlow<HomeState> get() = _state

    fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.OpenCamera -> _state.value = HomeState.CameraOpen
            is HomeIntent.OpenGallery -> _state.value = HomeState.Idle  // 갤러리 선택 UI로 이동
            is HomeIntent.CloseCamera -> _state.value = HomeState.Idle
            is HomeIntent.ImageCaptured -> _state.value = HomeState.ImageCaptured(intent.imagePath)
            is HomeIntent.ImageSelected -> _state.value = HomeState.ImageCaptured(intent.imagePath)
            is HomeIntent.ProcessOcr -> processOcr(intent.imagePath)
            is HomeIntent.OcrCompleted -> _state.value = HomeState.OcrSuccess(intent.text)
            is HomeIntent.OcrFailed -> _state.value = HomeState.Error(intent.error)
        }
    }

    private fun processOcr(imagePath: String) {
        _state.value = HomeState.OcrProcessing(imagePath)

        viewModelScope.launch {
            try {
                val result = ocrManager.recognizeText(imagePath)
                result.text.let {
                    handleIntent(HomeIntent.OcrCompleted(it))
                }
            } catch (e: Exception) {
                handleIntent(HomeIntent.OcrFailed(e.localizedMessage ?: "OCR failed"))
            }
        }
    }
}

