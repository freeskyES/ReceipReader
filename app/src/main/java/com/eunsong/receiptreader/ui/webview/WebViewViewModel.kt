package com.eunsong.receiptreader.ui.webview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eunsong.receiptreader.data.Todo
import com.eunsong.receiptreader.data.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
) : ViewModel() {
    private val _fetchTrigger = MutableStateFlow(false)
    val webViewState: StateFlow<WebViewState> =
        _fetchTrigger
            .flatMapLatest {
                todoRepository.findAll()
                    .map<List<Todo>, WebViewState> { todos -> WebViewState.Success(todos) }
                    .onStart { emit(WebViewState.Loading) }
                    .catch { emit(WebViewState.Error("Failed to load data")) }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = WebViewState.Loading,
            )

    fun processIntent(intent: WebViewIntent) {
        when (intent) {
            is WebViewIntent.FetchData -> fetchData()
        }
    }

    private fun fetchData() {
        _fetchTrigger.value = !_fetchTrigger.value
    }
}
