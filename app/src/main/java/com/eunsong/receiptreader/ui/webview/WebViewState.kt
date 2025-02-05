package com.eunsong.receiptreader.ui.webview

import com.eunsong.receiptreader.data.Todo


sealed class WebViewState {
    data object Loading : WebViewState() // 로딩 중
    data class Success(
        val todos: List<Todo>,
    ) : WebViewState()

    data class Error(val message: String) : WebViewState() // 오류 발생
}
