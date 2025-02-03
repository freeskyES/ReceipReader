package com.eunsong.receiptreader.ui.webview.bridge


import com.eunsong.receiptreader.ui.webview.WebViewIntent
import com.eunsong.receiptreader.ui.webview.bridge.dto.response.ErrorResponse
import com.eunsong.receiptreader.ui.webview.bridge.dto.response.WebViewResponse
import com.google.gson.Gson
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class WebViewBridgeTest {

    @Mock
    lateinit var webViewCaller: WebViewCaller

    @Mock
    lateinit var callback: (WebViewIntent) -> Unit

    private lateinit var webViewBridge: WebViewBridge
    private val gson = Gson()

    @Captor
    lateinit var intentCaptor: ArgumentCaptor<WebViewIntent>

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        intentCaptor = ArgumentCaptor.forClass(WebViewIntent::class.java)
        webViewBridge = WebViewBridge(webViewCaller, callback)
    }

    @Test
    fun `getTodos should call onError when JSON parsing fails`() {
        // Given: An invalid JSON string
        val invalidJson = "{invalid json}"

        // When
        webViewBridge.getTodos(invalidJson)

        // Then
        val expectedErrorJson = gson.toJson(ErrorResponse("Error"))
        val expectedJsCode = "javascript:onError($expectedErrorJson)"

        verify(webViewCaller).loadUrl(expectedJsCode)
    }

    @Test
    fun `onError should send error response to WebView`() {
        // Given: An error message
        val errorMessage = "Test Error"

        // When
        webViewBridge.onError(errorMessage)

        // Then
        val expectedErrorJson = gson.toJson(ErrorResponse(errorMessage))
        val expectedJsCode = "javascript:onError($expectedErrorJson)"

        verify(webViewCaller).loadUrl(expectedJsCode)
    }

    @Test
    fun `onSuccess should send success response to WebView`() {
        // Given: Some response data
        val responseData = mapOf("key" to "value")

        // When
        webViewBridge.onSuccess(responseData)

        // Then
        val expectedResponseJson = gson.toJson(WebViewResponse(responseData))
        val expectedJsCode = "javascript:onSuccess($expectedResponseJson)"

        verify(webViewCaller).loadUrl(expectedJsCode)
    }
}
