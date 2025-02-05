package com.eunsong.receiptreader.ui.webview.bridge

import android.webkit.JavascriptInterface
import com.eunsong.receiptreader.ui.webview.WebViewIntent
import com.eunsong.receiptreader.ui.webview.bridge.dto.request.TodoRequest
import com.eunsong.receiptreader.ui.webview.bridge.dto.response.ErrorResponse
import com.eunsong.receiptreader.ui.webview.bridge.dto.response.WebViewResponse
import com.google.gson.Gson

interface WebViewCaller {
    fun loadUrl(url: String)
}

class WebViewBridge(
    private val webView: WebViewCaller,
    private val callback: (WebViewIntent) -> Unit,
) {
    private val gson = Gson()

    @JavascriptInterface
    fun getTodos(body: String) {
        try {
            val request = gson.fromJson(body, TodoRequest::class.java)
            callback(WebViewIntent.FetchData(request.filter))
        } catch (e: Exception) {
            onError("Error")
        }
    }

    fun onError(message: String) {
        val jsonData = gson.toJson(ErrorResponse(message))
        val jsCode = "javascript:onError($jsonData)"
        webView.loadUrl(jsCode)
    }

    fun onSuccess(data: Any) {
        val jsonData = gson.toJson(WebViewResponse(data))
        val jsCode = "javascript:onSuccess($jsonData)"
        webView.loadUrl(jsCode)
    }
}
