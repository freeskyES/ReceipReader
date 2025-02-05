package com.eunsong.receiptreader.ui.webview

sealed class WebViewIntent {
    data class FetchData(val request: String) : WebViewIntent()
}
