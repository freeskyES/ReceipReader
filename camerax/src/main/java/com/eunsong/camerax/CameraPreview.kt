package com.eunsong.camerax

import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun CameraPreview(
    onImageCaptured: (String) -> Unit,
    onError: (Throwable) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }
    val fileProvider = { File(context.externalCacheDir, "captured_image.jpg") }
    val cameraCaptureManager = remember {
        CameraCaptureManager(lifecycleOwner, previewView, fileProvider)
    }

    LaunchedEffect(Unit) {
        cameraCaptureManager.startCamera()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        Button(
            onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    val result = cameraCaptureManager.captureImage()
                    result.fold(
                        onSuccess = { imagePath -> onImageCaptured(imagePath) },
                        onFailure = { error -> onError(error) }
                    )
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text("Capture Image")
        }
    }
}
