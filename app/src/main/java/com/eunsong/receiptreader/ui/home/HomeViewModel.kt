package com.eunsong.receiptreader.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow<HomeState>(HomeState.Idle)
    val state: StateFlow<HomeState> get() = _state

    fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.OpenCamera -> _state.value = HomeState.CameraOpen
            is HomeIntent.CloseCamera -> _state.value = HomeState.Idle
            is HomeIntent.CaptureImage -> _state.value = HomeState.ImageCaptured(intent.bitmap)
            is HomeIntent.ShowError -> _state.value = HomeState.Error(intent.message)
        }
    }
}

