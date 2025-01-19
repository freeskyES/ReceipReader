package com.eunsong.camerax

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import kotlin.coroutines.resume

class CameraCaptureManager(
    private val lifecycleOwner: LifecycleOwner,
    private val previewView: PreviewView,
    private val fileProvider: () -> File // 캡처 파일 경로 제공
) : CameraCapture {

    private var imageCapture: ImageCapture? = null
    private lateinit var cameraProvider: ProcessCameraProvider

    override fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(previewView.context)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().apply {
                setSurfaceProvider(previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
        }, ContextCompat.getMainExecutor(previewView.context))
    }

    override suspend fun captureImage(): Result<Bitmap> =
        suspendCancellableCoroutine { continuation ->
            val outputFile = fileProvider()
            val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()

            imageCapture?.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(previewView.context),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        val bitmap = BitmapFactory.decodeFile(outputFile.absolutePath)
                        continuation.resume(Result.success(bitmap))
                    }

                    override fun onError(exception: ImageCaptureException) {
                        continuation.resume(Result.failure(exception))
                    }
                }
            )
                ?: continuation.resume(Result.failure(IllegalStateException("ImageCapture is not initialized")))
        }

    override fun stopCamera() {
        cameraProvider.unbindAll()
    }
}
